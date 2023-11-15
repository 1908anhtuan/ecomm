package fontys.individual.school;
import fontys.individual.school.business.exception.*;
import fontys.individual.school.business.impl.BiddingAdvertServiceImpl;
import fontys.individual.school.domain.Account;
import fontys.individual.school.domain.BiddingAdvert;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.BiddingAdvertRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.BiddingAdEntity;
import fontys.individual.school.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class BiddingAdvertServiceTest {
    @Test
    public void testDeleteBiddingAdvert_ExistingAdvert() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);

        Long advertId = 1L;
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(new BiddingAdEntity()));
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        boolean result = biddingAdvertService.deleteBiddingAdvert(advertId);

        Assertions.assertTrue(result);

        Mockito.verify(repository).findById(advertId);
        Mockito.verify(repository).deleteById(advertId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testDeleteBiddingAdvert_NonExistingAdvert() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);

        Long advertId = 1L;

        Mockito.when(repository.findById(advertId)).thenReturn(Optional.empty());

        boolean result = biddingAdvertService.deleteBiddingAdvert(advertId);

        Assertions.assertFalse(result);

        Mockito.verify(repository).findById(advertId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testGetBiddingAdvertDetails_ExistingAdvert() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        Long advertId = 1L;

        BiddingAdEntity advertEntity = new BiddingAdEntity();
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(advertEntity));

        BiddingAdvert biddingAdvert = new BiddingAdvert();
        biddingAdvert.setBidExpirationDate(null);
        biddingAdvert.setTitle(advertEntity.getTitle());
        biddingAdvert.setProductDescription(advertEntity.getProductDescription());
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);

        Optional<BiddingAdvert> result = biddingAdvertService.getBiddingAdvertDetails(advertId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(biddingAdvert, result.get());

        Mockito.verify(repository).findById(advertId);

    }
    @Test
    public void testGetBiddingAdvertDetails_NonExistingAdvert() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);

        Long advertId = 1L;

        Mockito.when(repository.findById(advertId)).thenReturn(Optional.empty());

        Optional<BiddingAdvert> result = biddingAdvertService.getBiddingAdvertDetails(advertId);

        Assertions.assertFalse(result.isPresent());

        Mockito.verify(repository).findById(advertId);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    public void testBuyBiddingAdvert_NonExistingAdvert(){
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;

        BuyNowBiddingAdvertRequest request = new BuyNowBiddingAdvertRequest();

        Mockito.when(repository.findById(advertId)).thenReturn(Optional.empty());

        Assertions.assertThrows(AdvertNotFoundException.class, () -> {biddingAdvertService.buyBiddingAdvert(advertId, request);});

    }

    @Test
    public void testBuyBiddingAdvert_NonExistingCustomer(){
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;
        Long userId = 1L;
        BuyNowBiddingAdvertRequest request = new BuyNowBiddingAdvertRequest();
        BiddingAdEntity advertEntity = BiddingAdEntity.builder().id(advertId).build();
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(advertEntity));

        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {biddingAdvertService.buyBiddingAdvert(advertId, request);});

    }


    @Test
    public void testBuyBiddingAdvert_UserNotFound(){
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;
        Long userId = 1L;
        BuyNowBiddingAdvertRequest request = new BuyNowBiddingAdvertRequest();
        BiddingAdEntity advertEntity = BiddingAdEntity.builder().id(advertId).build();
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(advertEntity));

        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {biddingAdvertService.buyBiddingAdvert(advertId, request);});

    }

    @Test
    public void testBuyingBiddingAdvert_InsufficientBalance(){
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;

        Long userId = 100L;
        AccountEntity userAccountEntity = new AccountEntity();
        userAccountEntity.setId(userId);
        userAccountEntity.setBalance(20.0); // Set the user's account balance

        BuyNowBiddingAdvertRequest request = BuyNowBiddingAdvertRequest.builder().userId(userId).build();

        BiddingAdEntity advertEntity = BiddingAdEntity.builder().id(advertId).binPrice(30).build();
        when(repository.findById(advertId)).thenReturn(Optional.of(advertEntity));
        when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccountEntity));
        Assertions.assertThrows(InsufficientBalanceException.class, () -> {biddingAdvertService.buyBiddingAdvert(advertId, request);});

        verify(repository, times(1)).findById(advertId);
        verify(accountRepository, times(1)).findById(userId);
    }

    @Test
    public void testBuyingBiddingAdvert_AdvertExpired(){
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;

        Long userId = 100L;
        AccountEntity userAccountEntity = new AccountEntity();
        userAccountEntity.setId(userId);
        userAccountEntity.setBalance(20.0); // Set the user's account balance

        BuyNowBiddingAdvertRequest request = BuyNowBiddingAdvertRequest.builder().userId(userId).build();

        BiddingAdEntity advertEntity = BiddingAdEntity.builder().id(advertId).binPrice(10).isEnded(true).build();
        when(repository.findById(advertId)).thenReturn(Optional.of(advertEntity));
        when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccountEntity));
        Assertions.assertThrows(ResourceNotAvailableException.class, () -> {biddingAdvertService.buyBiddingAdvert(advertId, request);});

        verify(repository, times(1)).findById(advertId);
        verify(accountRepository, times(1)).findById(userId);
    }


    @Test
    public void testCreateBiddingAdvert_Successful() {
        // Arrange
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        CreateBiddingAdvertRequest request = CreateBiddingAdvertRequest.builder()
                .title("Test Advert")
                .productDescription("This is a test advert.")
                .category(CategoryEntity.builder().id(1L).name("lll").description("dw").build())
                .advertiser(AccountEntity.builder().id(1L).build())
                .lowestBidAmountAllowed(10)
                .initialBidPrice(20)
                .binPrice(50)
                .bidExpirationDate(LocalDateTime.now().plusDays(7))
                .build();

        ArgumentCaptor<BiddingAdEntity> captor = ArgumentCaptor.forClass(BiddingAdEntity.class);
        when(repository.save(any())).thenReturn(BiddingAdEntity.builder().build());

        // Act
        CreateBiddingAdvertResponse response = biddingAdvertService.createBiddingAdvert(request);

        // Assert
        verify(repository).save(captor.capture());
        BiddingAdEntity savedAdvertEntity = captor.getValue();
        Assertions.assertNotNull(savedAdvertEntity);
        Assertions.assertEquals(request.getTitle(), savedAdvertEntity.getTitle());
        Assertions.assertEquals(request.getProductDescription(), savedAdvertEntity.getProductDescription());

        Assertions.assertNotNull(response);
        BiddingAdvert createdAdvert = response.getBiddingAdvert();
        Assertions.assertNotNull(createdAdvert);

        verify(repository).save(any(BiddingAdEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testCreateBiddingAdvert_NullRequest() {
        // Act and Assert
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Assertions.assertThrows(NullPointerException.class, () -> {
            biddingAdvertService.createBiddingAdvert(null);
        });

        verifyNoInteractions(repository);
    }



    @Test
    public void testCreateBiddingAdvert_InvalidBinPrice() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        CreateBiddingAdvertRequest request = new CreateBiddingAdvertRequest();
        request.setTitle("Test Advert");
        request.setProductDescription("This is a test advert.");
        request.setBidExpirationDate(LocalDateTime.now().plusDays(5));
        request.setBinPrice(3);

        Assertions.assertThrows(BadRequestException.class, () -> {
            biddingAdvertService.createBiddingAdvert(request);
        });
    }

    @Test
    public void testCreateBiddingAdvert_InvalidTitle() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        CreateBiddingAdvertRequest request = new CreateBiddingAdvertRequest();
        request.setTitle("Ad");
        request.setProductDescription("This is a test advert.");
        request.setBidExpirationDate(LocalDateTime.now().plusDays(5));
        request.setBinPrice(10);

        Assertions.assertThrows(BadRequestException.class, () -> {
            biddingAdvertService.createBiddingAdvert(request);
        });
    }

    @Test
    public void testCreateBiddingAdvert_InvalidProductDescription() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        CreateBiddingAdvertRequest request = new CreateBiddingAdvertRequest();
        request.setTitle("Test Advert");
        request.setProductDescription("Short description");
        request.setBidExpirationDate(LocalDateTime.now().plusDays(5));
        request.setBinPrice(10);

        Assertions.assertThrows(BadRequestException.class, () -> {
            biddingAdvertService.createBiddingAdvert(request);
        });
    }

    @Test
    public void testCreateBiddingAdvert_InvalidBidExpirationDate() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        CreateBiddingAdvertRequest request = new CreateBiddingAdvertRequest();
        request.setTitle("Test Advert");
        request.setProductDescription("This is a test advert.");
        request.setBidExpirationDate(LocalDateTime.now().plusDays(2));
        request.setBinPrice(10);

        Assertions.assertThrows(BadRequestException.class, () -> {
            biddingAdvertService.createBiddingAdvert(request);
        });
    }

    @Test
    public void testBidAdvert_ValidRequest() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;
        Long userId = 1L;
        double bidAmount = 100.0;

        BiddingAdEntity adEntity = new BiddingAdEntity();
        adEntity.setBidExpirationDate(LocalDateTime.now().plusDays(10));
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(adEntity));
        BiddingAdvert ad = new BiddingAdvert();
        Mockito.when(repository.save(Mockito.any(BiddingAdEntity.class))).thenReturn(adEntity);

        AccountEntity userAccountEntity = new AccountEntity();
        userAccountEntity.setId(userId);
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccountEntity));
        Account account = new Account();

        BidRequest request = new BidRequest();
        request.setUserId(userId);
        request.setBidAmount(bidAmount);
        BidResponse response = biddingAdvertService.bidAdvert(advertId, request);

        // Assertions
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBiddingAdvert());
        Assertions.assertEquals("Successfully bid, you are now the owner of the item", response.getMessage());
    }

    @Test
    public void testBidAdvert_AdvertNotFound() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;
        BidRequest request = new BidRequest();
        request.setUserId(1L);
        request.setBidAmount(100.0);

        Mockito.when(repository.findById(advertId)).thenReturn(Optional.empty());

        Assertions.assertThrows(AdvertNotFoundException.class, () -> {
            biddingAdvertService.bidAdvert(advertId, request);
        });
    }

    @Test
    public void testBidAdvert_UserNotFound() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;
        Long userId = 1L;
        BidRequest request = new BidRequest();
        request.setUserId(userId);
        request.setBidAmount(100.0);

        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(new BiddingAdEntity()));
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            biddingAdvertService.bidAdvert(advertId, request);
        });
    }
    @Test
    public void testBidAdvert_InvalidBidAmount() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;        Long userId = 1L;
        double bidAmount = 0.0;

        BiddingAdEntity adEntity = new BiddingAdEntity();
        adEntity.setBidExpirationDate(LocalDateTime.now().plusDays(10));
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(adEntity));

        AccountEntity userAccountEntity = new AccountEntity();
        userAccountEntity.setId(1L);
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccountEntity));

        BidRequest request = new BidRequest();
        request.setUserId(userId);
        request.setBidAmount(bidAmount);

        Assertions.assertThrows(BidException.class, () -> {
            biddingAdvertService.bidAdvert(advertId, request);
        });
    }

    @Test
    public void testGetAllFilteredBiddingAds() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        GetAllBiddingAdvertsByFilterRequest request = new GetAllBiddingAdvertsByFilterRequest();
        request.setCategoryId(1L);
        request.setVerificationStatus(VerificationStatus.Verified);
        request.setAdStatus(AdStatus.Available);

        List<BiddingAdEntity> adEntities = new ArrayList<>();
        Mockito.when(repository.findByCriteria(request.getCategoryId(), request.getVerificationStatus(), request.getAdStatus())).thenReturn(adEntities);

        GetAllBiddingAdvertsByFilterResponse response = biddingAdvertService.getAllFilteredBiddingAds(request);

        // Assertions
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getFilteredBiddingAdList());
    }

    @Test
    public void testSetAdStatus_AdvertNotFound() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        SetAdvertStatusRequest request = new SetAdvertStatusRequest();
        request.setId(1L);
        request.setVerificationStatus(VerificationStatus.Verified);
        request.setAdvertStatus(AdStatus.Available);

        Mockito.when(repository.findById(request.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(AdvertNotFoundException.class, () -> {
            biddingAdvertService.setAdStatus(request);
        });
    }
    @Test
    public void testSetAdStatus_NoUpdateRequired() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        Long advertId = 1L;
        SetAdvertStatusRequest request = new SetAdvertStatusRequest();
        request.setId(advertId);
        request.setVerificationStatus(null);
        request.setAdvertStatus(null);

        BiddingAdEntity adEntity = new BiddingAdEntity();
        Mockito.when(repository.findById(advertId)).thenReturn(Optional.of(adEntity));
        Mockito.when(repository.updateAdStatus(Mockito.anyLong(), Mockito.any(), Mockito.any())).thenReturn(0);

        SetAdvertStatusResponse response = biddingAdvertService.setAdStatus(request);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.getIsUpdated());
    }
    @Test
    public void testGetPersonalBiddingAdverts_UserNotFound() {
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);
        GetPersonalBiddingAdsRequest request = new GetPersonalBiddingAdsRequest();
        request.setId(1L);

        Mockito.when(accountRepository.findById(request.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            biddingAdvertService.getPersonalBiddingAdverts(request);
        });
    }

    @Test
    public void testGetPersonalRegularAdverts_ReturnsPersonalAdverts() {
        // Arrange
        BiddingAdvertRepository repository = mock(BiddingAdvertRepository.class);
        AccountRepository accountRepository = mock(AccountRepository.class);
        BiddingAdvertServiceImpl biddingAdvertService = new BiddingAdvertServiceImpl(repository, accountRepository);

        long accountId = 1L;

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountId);
        accountEntity.setAccountName("testuser");

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

        List<BiddingAdEntity> advertEntities = new ArrayList<>();
        BiddingAdEntity advertEntity1 = new BiddingAdEntity();
        advertEntity1.setId(1L);
        advertEntity1.setTitle("Advert 1");
        advertEntities.add(advertEntity1);
        BiddingAdEntity advertEntity2 = new BiddingAdEntity();
        advertEntity2.setId(2L);
        advertEntity2.setTitle("Advert 2");
        advertEntities.add(advertEntity2);

        Mockito.when(repository.findAdsByHighestBidderAndStatus(accountId, true)).thenReturn(advertEntities);


        GetPersonalBiddingAdsRequest request = new GetPersonalBiddingAdsRequest(accountId);

        // Act
        GetPersonalBiddingAdsResponse response = biddingAdvertService.getPersonalBiddingAdverts(request);

        // Assert
        assertNotNull(response);
        List<BiddingAdvert> personalAdverts = response.getPersonalBiddingAdsList();
        assertEquals(advertEntities.size(), personalAdverts.size());
        assertEquals(advertEntities.get(0).getId(), personalAdverts.get(0).getId());
        assertEquals(advertEntities.get(0).getTitle(), personalAdverts.get(0).getTitle());
        assertEquals(advertEntities.get(1).getId(), personalAdverts.get(1).getId());
        assertEquals(advertEntities.get(1).getTitle(), personalAdverts.get(1).getTitle());

        verify(accountRepository, times(1)).findById(accountId);
        verify(repository, times(1)).findAdsByHighestBidderAndStatus(accountId,true);
    }

}
