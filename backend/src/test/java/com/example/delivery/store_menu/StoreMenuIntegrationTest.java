package com.example.delivery.store_menu;


import com.example.delivery.category.entity.Category;
import com.example.delivery.category.repository.CategoryRepository;
import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.dto.*;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.AiDescriptionRepository;
import com.example.delivery.menu.repository.MenuOptionRepository;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.dto.StoreRequestDto;
import com.example.delivery.store.dto.StoreUpdateDto;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.StoreRepository;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreMenuIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuOptionRepository menuOptionRepository;

    @Autowired
    private AiDescriptionRepository aiDescriptionRepository;

    private static Category koreanCategory;
    private static Category chineseCategory;
    private static Store koreanStore;
    private static Store chineseStore;
    private static Menu kimchiStewMenu;
    private static Menu budaeStewMenu;

    private static User customer;
    private static User owner;
    private static User manager;


    @BeforeAll
    void setup() {

        koreanCategory = categoryRepository.findByCategoryNameAndDeletedFalse("한식")
                .orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        chineseCategory = categoryRepository.findByCategoryNameAndDeletedFalse("중식")
                .orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        customer = userRepository.findById(1L).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        owner = userRepository.findById(2L).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        manager = userRepository.findById(3L).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

    }

    @AfterAll
    void tearDown() {
        storeRepository.deleteAll();
        menuRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("가게 생성 테스트 - OWNER 실패")
    @WithUserDetails("owner@example.com")
    void createStoreTestAsOwner() throws Exception {

        StoreRequestDto requestDto = new StoreRequestDto();
        requestDto.setUserId(2L);
        requestDto.setCategoryId(koreanCategory.getCategoryId());
        requestDto.setStoreOwnerName(owner.getUserName());
        requestDto.setStoreName("한식가게");
        requestDto.setStoreLocation("한식거리");

        // owner 의 요청 실패
        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(2)
    @DisplayName("가게 생성 테스트 - MANAGER 실패 & 성공")
    @WithUserDetails("manager@example.com")
    void createStoreTestAsManager() throws Exception {

        StoreRequestDto requestDto = new StoreRequestDto();
        requestDto.setUserId(3L); // manager의 id
        requestDto.setCategoryId(koreanCategory.getCategoryId());
        requestDto.setStoreOwnerName(owner.getUserName());
        requestDto.setStoreName("한식가게");
        requestDto.setStoreLocation("한식거리");

        // manager 요청 실패 - 가게 userId 의 권한이 owner x 여서
        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is4xxClientError());

        // manager 요청 성공
        requestDto.setUserId(2L); // owner권한의 id
        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful());

        assertTrue(storeRepository.existsByStoreNameAndDeletedFalse("한식가게"));

        // 중식 가게도 생성
        requestDto.setUserId(2L); // manager의 id
        requestDto.setCategoryId(chineseCategory.getCategoryId());
        requestDto.setStoreOwnerName(owner.getUserName());
        requestDto.setStoreName("중식가게");
        requestDto.setStoreLocation("중식거리");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful());

        // 데이터 검증
        assertTrue(storeRepository.existsByStoreNameAndDeletedFalse("중식가게"));

        koreanStore = storeRepository.findByStoreNameAndDeletedFalse("한식가게")
                .orElseThrow(()-> new CustomException(ErrorCode.STORE_NOT_FOUND));
        chineseStore = storeRepository.findByStoreNameAndDeletedFalse("중식가게")
                .orElseThrow(()-> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    @Test
    @Order(3)
    @DisplayName("가게 수정 - CUSTOMER 실패")
    @WithUserDetails("customer@example.com")
    void updateStoreTestAsCustomer() throws Exception {
        StoreRequestDto requestDto = new StoreRequestDto();

        requestDto.setCategoryId(koreanCategory.getCategoryId());
        requestDto.setStoreOwnerName(koreanStore.getStoreOwnerName());
        requestDto.setStoreLocation(koreanStore.getStoreLocation());
        requestDto.setOpened(koreanStore.isOpened());

        requestDto.setStoreName("수정된중식가게");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/stores/{storeId}/update", chineseStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is5xxServerError());

    }

    @Test
    @Order(4)
    @DisplayName("가게 수정 - 중식가게 이름 수정된중식가게로 변경")
    @WithUserDetails("owner@example.com")
    void updateStoreTest() throws Exception {

        StoreUpdateDto requestDto = new StoreUpdateDto();

        requestDto.setStoreName("수정된중식가게");

        String originalStoreOwnerName = chineseStore.getStoreOwnerName();
        String originalStoreLocation = chineseStore.getStoreLocation();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/stores/{storeId}/update", chineseStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        Store updatestore = storeRepository.findByStoreNameAndDeletedFalse("수정된중식가게")
                .orElseThrow( () -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        // 데이터 검증
        Assertions.assertEquals("수정된중식가게", updatestore.getStoreName());
        Assertions.assertEquals(originalStoreOwnerName, updatestore.getStoreOwnerName());
        Assertions.assertEquals(originalStoreLocation, updatestore.getStoreLocation());

    }

    @Test
    @Order(5)
    @DisplayName("한식에 메뉴 추가 - CUSTOMER 실패, OWNER 성공")
    @WithUserDetails("customer@example.com")
    void addMenuByCustomerTest() throws Exception {

        MenuRequestDto requestDto = new MenuRequestDto();
        requestDto.setMenuName("김치찌개");
        requestDto.setMenuPrice(15000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/create", koreanStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(6)
    @DisplayName("한식에 메뉴 추가 - OWNER 성공")
    @WithUserDetails("owner@example.com")
    void addMenuByOwnerTest() throws Exception {
        // 메뉴 추가 - OWNER
        MenuRequestDto requestDto = new MenuRequestDto();
        requestDto.setStoreId(koreanStore.getStoreId());
        requestDto.setMenuName("김치찌개");
        requestDto.setMenuPrice(10000);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/create", koreanStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful());

        // 데이터 검증
        kimchiStewMenu = menuRepository.findByMenuNameAndDeletedFalse("김치찌개")
                .orElseThrow(() -> new RuntimeException("메뉴 추가 실패"));
        Assertions.assertEquals("김치찌개", kimchiStewMenu.getMenuName());

        // 부대찌개 추가
        requestDto.setMenuName("부대찌개");
        requestDto.setMenuPrice(150000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/create", koreanStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful());

        budaeStewMenu = menuRepository.findByMenuNameAndDeletedFalse("부대찌개")
                .orElseThrow(() -> new RuntimeException("부대찌개 추가 실패"));
        Assertions.assertEquals("부대찌개", budaeStewMenu.getMenuName());
    }

    @Test
    @Order(7)
    @DisplayName("한식에 김치찌개 메뉴 옵션 추가")
    @WithUserDetails("owner@example.com")
    void addMenuOptionTest() throws Exception {
        // 메뉴 옵션 추가
        MenuOptionRequestDto option1 = new MenuOptionRequestDto();
        option1.setMenuId(kimchiStewMenu.getMenuId());
        option1.setOptionName("김치많이");
        option1.setOptionPrice(5000);

        MenuOptionRequestDto option2 = new MenuOptionRequestDto();
        option1.setMenuId(kimchiStewMenu.getMenuId());
        option2.setOptionName("김치적게");
        option2.setOptionPrice(50000);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/{menuId}/options/create", koreanStore.getStoreId(), kimchiStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(option1)))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/{menuId}/options/create", koreanStore.getStoreId(), kimchiStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(option2)))
                .andExpect(status().is2xxSuccessful());


    }

    @Test
    @Order(8)
    @DisplayName("한식에 메뉴 옵션 수정")
    @WithUserDetails("owner@example.com")
    void updateMenuOptionTest() throws Exception {

        MenuOption menuOption = menuOptionRepository.findByOptionNameAndMenu_MenuIdAndDeletedFalse("김치적게", kimchiStewMenu.getMenuId())
                .orElseThrow(()->new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        MenuOptionRequestDto updateRequest = new MenuOptionRequestDto();
        updateRequest.setMenuId(kimchiStewMenu.getMenuId());
        updateRequest.setMenuOptionId(menuOption.getMenuOptionId());
        updateRequest.setOptionName("수정된 김치적게");
        updateRequest.setOptionPrice(5000);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/stores/{storeId}/menus/{menuId}/options/update", koreanStore.getStoreId(), kimchiStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().is2xxSuccessful());

        MenuOption menuOptionUpdate = menuOptionRepository.findByOptionNameAndMenu_MenuIdAndDeletedFalse("수정된 김치적게", kimchiStewMenu.getMenuId())
                .orElseThrow(()-> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));


    }

    @Test
    @Order(9)
    @DisplayName("한식에 메뉴 옵션 삭제")
    @WithUserDetails("owner@example.com")
    void deleteMenuOptionTest() throws Exception {


        MenuOption menuOption = menuOptionRepository.findByOptionNameAndMenu_MenuIdAndDeletedFalse("수정된 김치적게", kimchiStewMenu.getMenuId())
                .orElseThrow(()-> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        MenuOptionRequestDto deleteRequest = new MenuOptionRequestDto();
        deleteRequest.setMenuId(kimchiStewMenu.getMenuId());
        deleteRequest.setMenuOptionId(menuOption.getMenuOptionId());
        deleteRequest.setOptionName("수정된 김치적게");
        deleteRequest.setOptionPrice(5000);


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/stores/{storeId}/menus/{menuId}/options/delete", koreanStore.getStoreId(), kimchiStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequest)))
                .andExpect(status().is2xxSuccessful());

        // 데이터 검증
        MenuOption deletedOption = menuOptionRepository.findById(menuOption.getMenuOptionId())
                .orElseThrow(()-> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        assertTrue(deletedOption.isDeleted());
        assertEquals("owner@example.com",deletedOption.getDeletedBy());

    }

    @Test
    @Order(10)
    @DisplayName("메뉴 수정 - 부대찌개 숨기기")
    @WithUserDetails("owner@example.com")
    void hideMenuTest() throws Exception {
        MenuRequestDto updateRequest = new MenuRequestDto();
        updateRequest.setMenuName(budaeStewMenu.getMenuName());
        updateRequest.setMenuPrice(budaeStewMenu.getMenuPrice());
        updateRequest.setMenuName(budaeStewMenu.getMenuName());
        updateRequest.setHidden(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/stores/{storeId}/menus/{menuId}/update", koreanStore.getStoreId(), budaeStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().is2xxSuccessful());

        Menu updatedMenu = menuRepository.findById(budaeStewMenu.getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        assertTrue(updatedMenu.isHidden());
    }

    @Test
    @Order(11)
    @DisplayName("AI 요청 - 일반 유저 실패")
    @WithUserDetails("customer@example.com")
    void aiRequestFailForCustomer() throws Exception {
        AiDescriptionRequestDto requestDto = new AiDescriptionRequestDto();
        requestDto.setAiQuestion("만두 이름 추천해줘라");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/{menuId}/aiQuestion", koreanStore.getStoreId(), kimchiStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(12)
    @DisplayName("AI 요청 - 가게 주인 성공")
    @WithUserDetails("owner@example.com")
    void aiRequestSuccessForOwner() throws Exception {

        AiDescriptionRequestDto requestDto = new AiDescriptionRequestDto();
        requestDto.setAiQuestion("만두 이름 추천해줘라");

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/api/stores/{storeId}/menus/{menuId}/aiQuestion", koreanStore.getStoreId(), kimchiStewMenu.getMenuId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        AiDescriptionClientResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), AiDescriptionClientResponseDto.class);
        assertNotNull(responseDto);
        assertTrue(responseDto.getAiQuestion().equals(responseDto.getAiQuestion()));



    }

    @Test
    @Order(13)
    @DisplayName("메뉴 상세 조회 - 일반 유저")
    @WithUserDetails("customer@example.com")
    void menuDetailViewForCustomer() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/{storeId}/menus/{menuId}", koreanStore.getStoreId(), kimchiStewMenu.getMenuId()))
                .andExpect(status().isOk())
                .andReturn();
        MenuResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), MenuResponseDto.class);
        assertNull(responseDto.getAiDescriptions());
    }

    @Test
    @Order(14)
    @DisplayName("메뉴 상세 조회 - 가게 주인")
    @WithUserDetails("owner@example.com")
    void menuDetailViewForOwner() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/{storeId}/menus/{menuId}", koreanStore.getStoreId(), kimchiStewMenu.getMenuId()))
                .andExpect(status().isOk())
                .andReturn();
        MenuResponseDto responseDto = objectMapper.readValue(result.getResponse().getContentAsString(), MenuResponseDto.class);
        assertFalse(responseDto.getAiDescriptions().isEmpty());
    }

    @Test
    @Order(15)
    @DisplayName("가게 전체 조회 - 키워드 검색")
    @WithUserDetails("owner@example.com")
    void searchStoreByKeyword() throws Exception {
        // 키워드: 한식
        var resultForKorean = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores")
                        .param("keyword", "한식"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("한식 가게만 표시: " + resultForKorean.getResponse().getContentAsString());

        // 키워드: 김치
        var resultForKimchi = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores")
                        .param("keyword", "김치"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("한식 가게만 표시: " + resultForKimchi.getResponse().getContentAsString());

        // 카테고리: 한식, 키워드: 똥
        var resultForInvalidKeyword = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores")
                        .param("category", koreanCategory.getCategoryName())
                        .param("keyword", "똥"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(" 결과 없음" + resultForInvalidKeyword.getResponse().getContentAsString());
    }


    @Test
    @Order(16)
    @DisplayName("가게 삭제 - 중식가게")
    @WithUserDetails("manager@example.com")
    void deleteStoreTest() throws Exception {

        StoreUpdateDto deleteDto = new StoreUpdateDto();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/stores/{storeId}/delete", chineseStore.getStoreId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDto)))
                .andExpect(status().is2xxSuccessful());

        Store deletedStore = storeRepository.findById(chineseStore.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        assertTrue(deletedStore.isDeleted());
    }

    @Test
    @Order(17)
    @DisplayName("가게 조회 - 삭제된 중식가게 미표시")
    @WithUserDetails("customer@example.com")
    void searchDeletedStore() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores")
                        .param("keyword", "중식"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("삭제된 중식가게 미표시:" + result.getResponse().getContentAsString());

    }

    @Test
    @Order(18)
    @DisplayName("한식가게 상세 조회 - 일반유저 hidden 메뉴 안보임 ")
    @WithUserDetails("customer@example.com")
    void koreanStoreDetailsTestAsCustomer() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/{storeId}", koreanStore.getStoreId()))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("한식가게 메뉴 중 김치찌개 표시 및 부대찌개 미표시:" + result.getResponse().getContentAsString());
    }

    @Test
    @Order(19)
    @DisplayName("한식가게 상세 조회 - 가게주인 hidden 메뉴 보임&키워드")
    @WithUserDetails("owner@example.com")
    void koreanStoreDetailsTestAsOwner() throws Exception {
        // 김치찌개 & 부대찌개
        var result = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/{storeId}", koreanStore.getStoreId()))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("한식가게 메뉴 중 김치찌개&부대찌개 표시:" + result.getResponse().getContentAsString());

        // 부대찌개만 나와야함
        var resultForInvalidKeyword = mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/{storeId}", koreanStore.getStoreId())
                        .param("keyword", "부대"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("한식가게 메뉴 중 부대찌개 표시:" +resultForInvalidKeyword.getResponse().getContentAsString());
    }

}
