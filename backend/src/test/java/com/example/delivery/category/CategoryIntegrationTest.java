package com.example.delivery.category;

import com.example.delivery.category.dto.CategoryRequestDto;
import com.example.delivery.category.entity.Category;
import com.example.delivery.category.repository.CategoryRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private static Category createdCategory;


    @AfterAll
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("카테고리 생성 - CUSTOMER/OWNER 실패")
    @WithUserDetails("customer@example.com")
    void createCategoryTestAsCustomer() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setCategoryName("TestCategory");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @Order(2)
    @DisplayName("카테고리 생성 - MANAGER 성공")
    @WithUserDetails("manager@example.com")
    void createCategoryTest() throws Exception {

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setCategoryName("한식");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        requestDto.setCategoryName("중식");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        createdCategory = categoryRepository.findByCategoryNameAndDeletedFalse("TestCategory123")
                .orElseThrow(() -> new RuntimeException("카테고리 생성 실패"));

        assertNotNull(createdCategory);

    }

    @Test
    @Order(3)
    @DisplayName("카테고리 수정 - 생성된 카테고리 수정")
    @WithUserDetails("manager@example.com")
    void updateCategoryTest() throws Exception {

        assertNotNull(createdCategory, "수정하려는 카테고리가 null입니다.");

        assertFalse(createdCategory.isDeleted(), "수정하려는 카테고리가 삭제된 상태입니다.");

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setCategoryId(createdCategory.getCategoryId());
        requestDto.setCategoryName("UpdatedCategory");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/categories/{categoryId}/update", createdCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());


        Category updatedCategory = categoryRepository.findByCategoryIdAndDeletedFalse(createdCategory.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
        assertEquals("UpdatedCategory", updatedCategory.getCategoryName());

    }

    @Test
    @Order(4)
    @DisplayName("카테고리 삭제 - 수정된 카테고리 삭제")
    @WithUserDetails("manager@example.com")
    void deleteCategoryTest() throws Exception {

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setCategoryId(createdCategory.getCategoryId());
        requestDto.setCategoryName(createdCategory.getCategoryName());


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/categories/{categoryId}/delete", createdCategory.getCategoryId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        Category deletedCategory = categoryRepository.findById(createdCategory.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        assertTrue(deletedCategory.isDeleted());
        assertEquals("manager@example.com",deletedCategory.getDeletedBy());
    }
}