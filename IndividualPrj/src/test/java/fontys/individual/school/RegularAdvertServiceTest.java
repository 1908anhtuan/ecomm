package fontys.individual.school;

import fontys.individual.school.business.exception.AdvertNotFoundException;
import fontys.individual.school.business.exception.BadRequestException;
import fontys.individual.school.business.exception.UserNotFoundException;
import fontys.individual.school.business.impl.RegularAdvertServiceImpl;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.RegularAdvert;
import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.RegularAdvertRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.RegularAdEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


    public class RegularAdvertServiceTest {
    @Test
    public void testDeleteRegularAdvert_ValidId_DeletesSuccessfully() {
        // Arrange
        RegularAdvertRepository repository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Long id = 1L;
        RegularAdEntity advertEntity = new RegularAdEntity();
        when(repository.findById(id)).thenReturn(Optional.of(advertEntity));

        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(repository, accountRepository);

        // Act
        Boolean result = regularAdvertService.deleteRegularAdvert(id);

        // Assert
        assertTrue(result);
        verify(repository, times(1)).deleteById(id);
    }
    @Test
    public void testDeleteRegularAdvert_InvalidId_NoDeletion() {
        // Arrange
        RegularAdvertRepository repository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(repository, accountRepository);

        // Act
        Boolean result = regularAdvertService.deleteRegularAdvert(id);

        // Assert
        assertFalse(result);
        verify(repository, never()).deleteById(id);
    }

    @Test
    public void testDeleteRegularAdvert_ExceptionOccurs_ReturnsFalse() {
        // Arrange
        RegularAdvertRepository repository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(new RegularAdEntity()));
        doThrow(new RuntimeException("Delete error")).when(repository).deleteById(id);

        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(repository, accountRepository);

        // Act
        Boolean result = regularAdvertService.deleteRegularAdvert(id);

        // Assert
        assertFalse(result);
        verify(repository, times(1)).deleteById(id);
    }


    @Test
    public void testGetRegularAdvertDetails_ValidId_ReturnsRegularAdvert() {
        // Arrange
        RegularAdvertRepository repository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Long id = 1L;
        RegularAdEntity advertEntity = new RegularAdEntity();
        when(repository.findById(id)).thenReturn(Optional.of(advertEntity));
        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(repository, accountRepository);
        RegularAdvert expectedRegularAdvert = new RegularAdvert(); // Create an expected RegularAdvert object

        // Act
        RegularAdvert regularAdvert = regularAdvertService.getRegularAdvertDetails(id).getRegularAdvert();

        // Assert
        assertTrue(regularAdvert != null);
        assertEquals(expectedRegularAdvert, regularAdvert);

        verify(repository, times(1)).findById(id);

    }

    @Test
    public void testGetRegularAdvertDetails_InvalidId_ReturnsEmptyOptional() {
        // Arrange
        RegularAdvertRepository repository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(repository, accountRepository);


        // Assert
        Assertions.assertThrows(AdvertNotFoundException.class, () ->
                regularAdvertService.getRegularAdvertDetails(id));
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testBuyRegularAdvert_SuccessfulPurchase() {
        // Arrange
        RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);

        Long advertId = 1L;
        RegularAdEntity advertEntity = new RegularAdEntity();
        advertEntity.setId(advertId);
        advertEntity.setPrice(10.0);

        Long userId = 100L;
        AccountEntity userAccountEntity = new AccountEntity();
        userAccountEntity.setId(userId);
        userAccountEntity.setBalance(20.0);


        BuyRegularAdvertRequest request = new BuyRegularAdvertRequest();
        request.setUserId(userId);


        when(regularAdvertRepository.findById(advertId)).thenReturn(Optional.of(advertEntity));
        when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccountEntity));
        when(regularAdvertRepository.save(Mockito.any(RegularAdEntity.class))).thenReturn(RegularAdEntity.builder().isSold(true).build());

        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

        BuyRegularAdvertResponse response = regularAdvertService.buyRegularAdvert(advertId, request);

        assertNotNull(response);
        RegularAdvert boughtAdvert = response.getRegularAdvert();
        assertNotNull(boughtAdvert);

        verify(regularAdvertRepository, times(1)).findById(advertId);
        verify(accountRepository, times(1)).findById(userId);
        verify(regularAdvertRepository, times(1)).save(Mockito.any(RegularAdEntity.class));


        assertTrue(boughtAdvert.isSold());
    }

    @Test
    public void testBuyRegularAdvert_AdvertNotFound() {
        // Arrange
        RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);

        Long advertId = 1L;
        BuyRegularAdvertRequest request = new BuyRegularAdvertRequest(123L);


        Mockito.when(regularAdvertRepository.findById(advertId)).thenReturn(Optional.empty());

        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

        Assertions.assertThrows(AdvertNotFoundException.class, () ->
                regularAdvertService.buyRegularAdvert(advertId, request));

        Mockito.verify(regularAdvertRepository).findById(advertId);
        Mockito.verifyNoInteractions(accountRepository);
        Mockito.verifyNoMoreInteractions(regularAdvertRepository);
    }
    @Test
    public void testBuyRegularAdvert_UserNotFound() {
        Long advertId = 1L;
        Long userId = 123L;
        BuyRegularAdvertRequest request = new BuyRegularAdvertRequest(userId);

        RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Mockito.when(regularAdvertRepository.findById(advertId)).thenReturn(Optional.of(new RegularAdEntity()));
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

        Assertions.assertThrows(UserNotFoundException.class, () ->
                regularAdvertService.buyRegularAdvert(advertId, request));

        Mockito.verify(regularAdvertRepository).findById(advertId);
        Mockito.verify(accountRepository).findById(userId);
        Mockito.verifyNoMoreInteractions(regularAdvertRepository);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }
        @Test
        public void testCreateRegularAdvert_WithValidInput_ShouldReturnCreateRegularAdvertResponse() {
            // Arrange
            CreateRegularAdvertRequest request = new CreateRegularAdvertRequest();
            request.setTitle("GoodTitle");
            request.setProductDescription("Good description that has more than 20 characters!!!!!!");
            request.setPrice(5);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdEntity savedRegularAdvertEntity = new RegularAdEntity();
            when(regularAdvertRepository.save(any(RegularAdEntity.class))).thenReturn(savedRegularAdvertEntity);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);
            // Act
            CreateRegularAdvertResponse response = regularAdvertService.createRegularAdvert(request);

            // Assert
            Assertions.assertNotNull(response);
            Assertions.assertNotNull(response.getRegularAdvert());
        }

        @Test
        public void testCreateRegularAdvert_WithInvalidInput_BadPrice_ShouldThrowException() {
            // Arrange
            CreateRegularAdvertRequest request = new CreateRegularAdvertRequest();
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            request.setPrice(4);
            assertThrows(BadRequestException.class, () -> regularAdvertService.createRegularAdvert(request));
        }

        @Test
        public void testCreateRegularAdvert_WithInvalidInput_BadTitle_ShouldThrowException() {
            // Arrange
            CreateRegularAdvertRequest request = new CreateRegularAdvertRequest();
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            request.setTitle("Noki");
            // Act & Assert
            assertThrows(BadRequestException.class, () -> regularAdvertService.createRegularAdvert(request));
        }

        @Test
        public void testCreateRegularAdvert_WithInvalidInput_BadDescription_ShouldThrowException() {
            // Arrange
            CreateRegularAdvertRequest request = new CreateRegularAdvertRequest();
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            request.setProductDescription("Too short description");
            // Act & Assert
            assertThrows(BadRequestException.class, () -> regularAdvertService.createRegularAdvert(request));
        }


        @Test
        public void testGetAll_WithExistingRegularAdverts_ShouldReturnGetAllRegularAdvertsResponseWithList() {
            // Arrange
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            List<RegularAdEntity> regularAdvertEntities = new ArrayList<>();
            regularAdvertEntities.add(RegularAdEntity.builder().build());
            regularAdvertEntities.add(RegularAdEntity.builder().build());


            when(regularAdvertRepository.findAll()).thenReturn(regularAdvertEntities);

            // Act
            GetAllRegularAdvertsResponse response = regularAdvertService.getAll();

            // Assert
            List<RegularAdvert> regularAdverts = response.getRegularAdvertList();
            assertEquals(regularAdvertEntities.size(), regularAdverts.size(), "The number of converted regular adverts should match the number of entities");
            assertNotNull(response);
            assertNotNull(response.getRegularAdvertList());
        }
        @Test
        public void testGetAll_WithNoRegularAdverts_ShouldReturnGetAllRegularAdvertsResponseWithEmptyList() {
            // Arrange
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            when(regularAdvertRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            GetAllRegularAdvertsResponse response = regularAdvertService.getAll();

            // Assert
            assertNotNull(response);
            assertNotNull(response.getRegularAdvertList());
            assertTrue(response.getRegularAdvertList().isEmpty());
        }
        @Test
        public void testDeleteRegularAdvert_WithExistingRegularAdvertId_ShouldReturnTrue() {
            // Arrange
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            Long regularAdvertId = 1L;
            Optional<RegularAdEntity> advertEntity = Optional.of(new RegularAdEntity());
            when(regularAdvertRepository.findById(regularAdvertId)).thenReturn(advertEntity);

            // Act
            boolean result = regularAdvertService.deleteRegularAdvert(regularAdvertId);

            // Assertt
            assertTrue(result);
            verify(regularAdvertRepository, times(1)).deleteById(regularAdvertId);
        }
        @Test
        public void testDeleteRegularAdvert_WithNonExistentRegularAdvertId_ShouldReturnFalse() {
            // Arrange
            AccountRepository accountRepository = mock(AccountRepository.class);
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            Long regularAdvertId = 1L;
            Optional<RegularAdEntity> advertEntity = Optional.empty();
            when(regularAdvertRepository.findById(regularAdvertId)).thenReturn(advertEntity);

            // Act
            boolean result = regularAdvertService.deleteRegularAdvert(regularAdvertId);

            // Assert
            assertFalse(result);
            verify(regularAdvertRepository, never()).deleteById(regularAdvertId);
        }

        @Test
        public void testCreateRegularAdvert_ValidRequest_CreatesRegularAdvert() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            CreateRegularAdvertRequest request = new CreateRegularAdvertRequest();
            request.setTitle("Test Advert");
            request.setProductDescription("This is a test advert");
            request.setPrice(10.0);

            RegularAdEntity savedAdvertEntity = new RegularAdEntity();
            savedAdvertEntity.setId(1L);
            savedAdvertEntity.setTitle("Test Advert");
            savedAdvertEntity.setProductDescription("This is a test advert");
            savedAdvertEntity.setPrice(10.0);

            Mockito.when(regularAdvertRepository.save(Mockito.any(RegularAdEntity.class))).thenReturn(savedAdvertEntity);

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            // Act
            CreateRegularAdvertResponse response = regularAdvertService.createRegularAdvert(request);

            // Assert
            assertNotNull(response);
            RegularAdvert createdAdvert = response.getRegularAdvert();
            assertNotNull(createdAdvert);
            assertEquals(savedAdvertEntity.getId(), createdAdvert.getId());
            assertEquals(savedAdvertEntity.getTitle(), createdAdvert.getTitle());
            assertEquals(savedAdvertEntity.getProductDescription(), createdAdvert.getProductDescription());
            assertEquals(savedAdvertEntity.getPrice(), createdAdvert.getPrice());

            verify(regularAdvertRepository, times(1)).save(Mockito.any(RegularAdEntity.class));
        }



        @Test
        public void testGetAllFilteredRegularAds_ReturnsFilteredAdverts() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            long categoryId = 1L;
            VerificationStatus verificationStatus = VerificationStatus.Verified;
            AdStatus adStatus = AdStatus.Available;

            List<RegularAdEntity> advertEntities = new ArrayList<>();
            RegularAdEntity advertEntity1 = new RegularAdEntity();
            advertEntity1.setId(1L);
            advertEntity1.setTitle("Advert 1");
            advertEntities.add(advertEntity1);
            RegularAdEntity advertEntity2 = new RegularAdEntity();
            advertEntity2.setId(2L);
            advertEntity2.setTitle("Advert 2");
            advertEntities.add(advertEntity2);

            Mockito.when(regularAdvertRepository.findByCriteria(categoryId, verificationStatus, adStatus)).thenReturn(advertEntities);

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            GetAllRegularAdvertsByFilterRequest request = new GetAllRegularAdvertsByFilterRequest(categoryId, verificationStatus, adStatus);

            // Act
            GetAllRegularAdvertsByFilterResponse response = regularAdvertService.getAllFilteredRegularAds(request);

            // Assert
            assertNotNull(response);
            List<RegularAdvert> filteredList = response.getFilteredRegularAdList();
            assertEquals(advertEntities.size(), filteredList.size());
            assertEquals(advertEntities.get(0).getId(), filteredList.get(0).getId());
            assertEquals(advertEntities.get(0).getTitle(), filteredList.get(0).getTitle());
            assertEquals(advertEntities.get(1).getId(), filteredList.get(1).getId());
            assertEquals(advertEntities.get(1).getTitle(), filteredList.get(1).getTitle());

            verify(regularAdvertRepository, times(1)).findByCriteria(categoryId, verificationStatus, adStatus);
        }

        @Test
        public void testSetAdStatus_ValidIdAndStatus_UpdatesAdStatus() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            long advertId = 1L;
            VerificationStatus verificationStatus = VerificationStatus.Verified;
            AdStatus adStatus = AdStatus.Unavailable;

            RegularAdEntity advertEntity = new RegularAdEntity();
            advertEntity.setId(advertId);
            advertEntity.setVerification(VerificationStatus.Pending);
            advertEntity.setAdStatus(AdStatus.Available);

            Mockito.when(regularAdvertRepository.findById(advertId)).thenReturn(Optional.of(advertEntity));
            Mockito.when(regularAdvertRepository.updateAdStatus(advertId, verificationStatus, adStatus)).thenReturn(1);

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            SetAdvertStatusRequest request = new SetAdvertStatusRequest(advertId, verificationStatus, adStatus);

            // Act
            SetAdvertStatusResponse response = regularAdvertService.setAdStatus(request);

            // Assert
            assertNotNull(response);
            assertTrue(response.getIsUpdated());

            verify(regularAdvertRepository, times(1)).findById(advertId);
            verify(regularAdvertRepository, times(1)).updateAdStatus(advertId, verificationStatus, adStatus);
        }

        @Test
        public void testSetAdStatus_InvalidId_ThrowsAdvertNotFoundException() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            long invalidAdvertId = 100L;
            VerificationStatus verificationStatus = VerificationStatus.Verified;
            AdStatus adStatus = AdStatus.Unavailable;

            Mockito.when(regularAdvertRepository.findById(invalidAdvertId)).thenReturn(Optional.empty());

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            SetAdvertStatusRequest request = new SetAdvertStatusRequest(invalidAdvertId, verificationStatus, adStatus);

            // Act and Assert
            assertThrows(AdvertNotFoundException.class, () -> regularAdvertService.setAdStatus(request));

            verify(regularAdvertRepository, times(1)).findById(invalidAdvertId);
            verify(regularAdvertRepository, times(0)).updateAdStatus(anyLong(), any(), any());
        }

        @Test
        public void testSetAdStatus_SameStatus_ReturnsNotUpdatedResponse() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            long advertId = 1L;
            VerificationStatus verificationStatus = VerificationStatus.Verified;
            AdStatus adStatus = AdStatus.Unavailable;

            RegularAdEntity advertEntity = new RegularAdEntity();
            advertEntity.setId(advertId);
            advertEntity.setVerification(verificationStatus);
            advertEntity.setAdStatus(adStatus);

            Mockito.when(regularAdvertRepository.findById(advertId)).thenReturn(Optional.of(advertEntity));

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            SetAdvertStatusRequest request = new SetAdvertStatusRequest(advertId, verificationStatus, adStatus);

            // Act
            SetAdvertStatusResponse response = regularAdvertService.setAdStatus(request);

            // Assert
            assertNotNull(response);
            assertFalse(response.getIsUpdated());

            verify(regularAdvertRepository, times(1)).findById(advertId);
            verify(regularAdvertRepository, times(0)).updateAdStatus(anyLong(), any(), any());
        }


        @Test
        public void testGetPersonalRegularAdverts_ReturnsPersonalAdverts() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            long accountId = 1L;

            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setId(accountId);
            accountEntity.setAccountName("testuser");

            Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

            List<RegularAdEntity> advertEntities = new ArrayList<>();
            RegularAdEntity advertEntity1 = new RegularAdEntity();
            advertEntity1.setId(1L);
            advertEntity1.setTitle("Advert 1");
            advertEntities.add(advertEntity1);
            RegularAdEntity advertEntity2 = new RegularAdEntity();
            advertEntity2.setId(2L);
            advertEntity2.setTitle("Advert 2");
            advertEntities.add(advertEntity2);

            Mockito.when(regularAdvertRepository.findByBuyerId(accountId)).thenReturn(advertEntities);

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            GetPersonalRegularAdsRequest request = new GetPersonalRegularAdsRequest(accountId);

            // Act
            GetPersonalRegularAdsResponse response = regularAdvertService.getPersonalRegularAdverts(request);

            // Assert
            assertNotNull(response);
            List<RegularAdvert> personalAdverts = response.getPersonalRegularAdsList();
            assertEquals(advertEntities.size(), personalAdverts.size());
            assertEquals(advertEntities.get(0).getId(), personalAdverts.get(0).getId());
            assertEquals(advertEntities.get(0).getTitle(), personalAdverts.get(0).getTitle());
            assertEquals(advertEntities.get(1).getId(), personalAdverts.get(1).getId());
            assertEquals(advertEntities.get(1).getTitle(), personalAdverts.get(1).getTitle());

            verify(accountRepository, times(1)).findById(accountId);
            verify(regularAdvertRepository, times(1)).findByBuyerId(accountId);
        }

        @Test
        public void testGetPersonalRegularAdverts_InvalidUserId_ThrowsUserNotFoundException() {
            // Arrange
            RegularAdvertRepository regularAdvertRepository = mock(RegularAdvertRepository.class);
            AccountRepository accountRepository = mock(AccountRepository.class);

            long invalidAccountId = 100L;

            Mockito.when(accountRepository.findById(invalidAccountId)).thenReturn(Optional.empty());

            RegularAdvertServiceImpl regularAdvertService = new RegularAdvertServiceImpl(regularAdvertRepository, accountRepository);

            GetPersonalRegularAdsRequest request = new GetPersonalRegularAdsRequest(invalidAccountId);

            assertThrows(UserNotFoundException.class, () -> regularAdvertService.getPersonalRegularAdverts(request));

            verify(accountRepository, times(1)).findById(invalidAccountId);
            verify(regularAdvertRepository, times(0)).findByBuyerId(anyLong());
        }
}
