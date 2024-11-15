package com.example.delivery.menu.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.code.ErrorCode;
import com.example.delivery.menu.dto.*;
import com.example.delivery.menu.entity.AiDescription;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import com.example.delivery.menu.repository.AiDescriptionRepository;
import com.example.delivery.menu.repository.MenuOptionRepository;
import com.example.delivery.menu.repository.MenuRepository;
import com.example.delivery.store.entity.Store;
import com.example.delivery.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final StoreRepository storeRepository;
    private final RestTemplate restTemplate;
    private final AiDescriptionRepository aiDescriptionRepository;
    @Value("${google.ai.key}")
    private String googleApiKey;
    @Value("${google.ai.url}")
    private String googleApiUrl;

    // 메뉴 등록
    @Transactional
    public MenuResponseDto createMenu(UUID storeId, MenuRequestDto menuRequestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        Menu menu = menuRequestDto.toEntity(store);
        menu = menuRepository.save(menu);

        return new MenuResponseDto(menu);
    }

    // 메뉴 상세 조회
    @Transactional(readOnly = true)
    public MenuResponseDto getMenuDetails(UUID storeId, UUID menuId) {

        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        return new MenuResponseDto(menu);
    }

    // 메뉴 수정
    @Transactional
    public void updateMenu(UUID storeId, UUID menuId, MenuRequestDto menuRequestDto) {

        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

            menu.update(menuRequestDto);

    }

    // 메뉴 삭제
    @Transactional
    public void deleteMenu(UUID storeId, UUID menuId, String username) {
        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        menu.delete(username);

    }

    // 메뉴 옵션 등록
    @Transactional
    public MenuOptionResponseDto createMenuOption(UUID storeId, UUID menuId, MenuOptionRequestDto menuOptionRequestDto) {

        Menu menu = menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        MenuOption menuOption = menuOptionRequestDto.toEntity(menu);

        menu.getMenuOptions().add(menuOption);

        menuOptionRepository.save(menuOption);
        menuRepository.save(menu);

        return new MenuOptionResponseDto(menuOption);

    }

    // 메뉴 옵션 수정
    @Transactional
    public void updateMenuOption(UUID storeId, UUID menuId, MenuOptionRequestDto menuOptionRequestDto) {

        menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        UUID menuOptionId = menuOptionRequestDto.getMenuOptionId();

        MenuOption menuOption = menuOptionRepository.findByMenuOptionIdAndDeletedFalse(menuOptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

            menuOption.update(menuOptionRequestDto);
    }

    // 메뉴 옵션 삭제
    @Transactional
    public void deleteMenuOption(UUID storeId, UUID menuId, UUID menuOptionId, String username) {

        menuRepository.findByMenuIdAndStore_StoreIdAndDeletedFalse(menuId, storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        MenuOption menuOption = menuOptionRepository.findByMenuOptionIdAndDeletedFalse(menuOptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_OPTION_NOT_FOUND));

        menuOption.delete(username);

    }

    public AiDescriptionClientResponseDto createAiDescription(UUID storeId, UUID menuId, AiDescriptionRequestDto aiDescriptionRequestDto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new CustomException(ErrorCode.MENU_NOT_FOUND));

        URI uri = UriComponentsBuilder
                .fromUriString(googleApiUrl)
                .queryParam("key", googleApiKey)
                .encode()
                .build()
                .toUri();

        String answerMessage = "답변을 최대한 간결하게 50자 이하로";
        String requestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", aiDescriptionRequestDto.getAiQuestion() + answerMessage);

        System.out.println(requestBody);

        RequestEntity<String> requestEntity = RequestEntity
                .post(uri)
                .header("Content-Type", "application/json")
                .body(requestBody);

        ResponseEntity<AiDescriptionResponseDto> responseEntity = restTemplate.exchange(requestEntity, AiDescriptionResponseDto.class);

        System.out.println("Response status: " + responseEntity.getStatusCode());
        System.out.println("Response body: " + responseEntity.getBody());

        AiDescriptionResponseDto aiDescriptionResponseDto = responseEntity.getBody();
        String aiAnswer = aiDescriptionResponseDto.getCandidates().get(0).getContent().getParts().get(0).getText();

        // 데이터 저장
        aiDescriptionRepository.save(
                AiDescription.builder()
                        .aiQuestion(aiDescriptionRequestDto.getAiQuestion())
                        .aiAnswer(aiAnswer)
                        .menu(menu)
                        .build()
        );

        return new AiDescriptionClientResponseDto(aiDescriptionRequestDto, aiAnswer);
    }

}
