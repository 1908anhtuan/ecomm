package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.BiddingAdvertUseCases;
import fontys.individual.school.business.exception.*;
import fontys.individual.school.business.impl.Converter.AccountConverter;
import fontys.individual.school.business.impl.Converter.BiddingAdvertConverter;
import fontys.individual.school.domain.Account;
import fontys.individual.school.domain.BiddingAdvert;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.BiddingAdvertRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.BiddingAdEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class BiddingAdvertServiceImpl implements BiddingAdvertUseCases {
    private final BiddingAdvertRepository biddingAdvertRepository;
    private final AccountRepository accountRepository;


    @Override
    public CreateBiddingAdvertResponse createBiddingAdvert(CreateBiddingAdvertRequest request) {
        BiddingAdEntity savedBiddingAdvert = saveAdvert(request);
        if(request.getBinPrice() < 5){
            throw new BadRequestException("Minimum bin price is 5");
        }

        if(request.getTitle().length() < 5){
            throw new BadRequestException("Title is too short!");
        }
        if(request.getProductDescription().length() < 20){
            throw  new BadRequestException("Description is too short!");
        }
        if(request.getBidExpirationDate().isBefore(LocalDateTime.now().plusDays(3))){
            throw new BadRequestException("Bid expiration date must be more than 3 days after order date");
        }
        return CreateBiddingAdvertResponse.builder()
                .biddingAdvert(BiddingAdvertConverter.builder().build().convert(savedBiddingAdvert))
                .build();
    }

    @Override
    public GetAllBiddingAdvertsResponse getAll() {
        List<BiddingAdEntity> results = biddingAdvertRepository.findAll();

        List<BiddingAdvert> biddingAdverts = results
                .stream()
                .map(BiddingAdvertConverter.builder().build()::convert)
                .toList();

        return GetAllBiddingAdvertsResponse.builder()
                .biddingAdvertList(biddingAdverts)
                .build();
    }

    @Override
    public Boolean deleteBiddingAdvert(Long id) {
        Optional<BiddingAdEntity> advertEntity = biddingAdvertRepository.findById(id);
        if(advertEntity.isPresent()){
            biddingAdvertRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Optional<BiddingAdvert> getBiddingAdvertDetails(Long id) {
        Optional<BiddingAdEntity> advertEntity = biddingAdvertRepository.findById(id);
        if (advertEntity.isPresent()) {
            biddingAdvertRepository.incrementViewCount(id);
            BiddingAdvertConverter converter = BiddingAdvertConverter.builder().build();
            BiddingAdvert biddingAdvert = converter.convert(advertEntity.get());
            return Optional.of(biddingAdvert);
        }
        return Optional.empty();
    }

    @Override
    public BuyNowBiddingAdvertResponse buyBiddingAdvert(Long adId, BuyNowBiddingAdvertRequest request) {
        Optional<BiddingAdEntity> adEntity = biddingAdvertRepository.findById(adId);
        if (!adEntity.isPresent()) {
            throw new AdvertNotFoundException();
        }

        BiddingAdvert ad = new BiddingAdvertConverter().convert(adEntity.get());

        Optional<AccountEntity> userAccountEntity = accountRepository.findById(request.getUserId());
        if (!userAccountEntity.isPresent()) {
            throw new UserNotFoundException();
        }

        Account account = AccountConverter.convertToAccount(userAccountEntity.get());
        if (account.getBalance() < ad.getBinPrice()) {
            throw new InsufficientBalanceException();
        }

        account.setBalance(account.getBalance() - ad.getBinPrice());

        if (!ad.buyInstantly(account)) {
            throw new ResourceNotAvailableException();
        }
        AccountEntity toBeSavedAccountEntity = AccountConverter.convertToAccountEntity(account);
        accountRepository.save(toBeSavedAccountEntity);
        BiddingAdEntity toBeSavedAdEntity = new BiddingAdvertConverter().convertToEntity(ad);
        BiddingAdEntity savedAdEntity = biddingAdvertRepository.save(toBeSavedAdEntity);
        return BuyNowBiddingAdvertResponse.builder().biddingAdvert(new BiddingAdvertConverter().convert(savedAdEntity)).build();
    }

    @Override
    public BidResponse bidAdvert(Long id,BidRequest request) {
        Optional<BiddingAdEntity> adEntity = biddingAdvertRepository.findById(id);
        if (!adEntity.isPresent()) {
            throw new AdvertNotFoundException();
        }

        BiddingAdvert ad = new BiddingAdvertConverter().convert(adEntity.get());

        Optional<AccountEntity> userAccountEntity = accountRepository.findById(request.getUserId());
        if (!userAccountEntity.isPresent()) {
            throw new UserNotFoundException();
        }

        Account account = AccountConverter.convertToAccount(userAccountEntity.get());

        if(!ad.placeBid(request.getBidAmount(), account)){
            throw new BidException();
        }
        String message = "Successfully bid";
        if(ad.isSold()){
            message = "Successfully bid, you are now the owner of the item";
        }
        BiddingAdEntity toBeSavedAdEntity = new BiddingAdvertConverter().convertToEntity(ad);
        BiddingAdEntity savedAdEntity = biddingAdvertRepository.save(toBeSavedAdEntity);

        return BidResponse.builder().biddingAdvert(new BiddingAdvertConverter().convert(savedAdEntity)).message(message).build();

    }

    @Override
    public GetAllBiddingAdvertsByFilterResponse getAllFilteredBiddingAds(GetAllBiddingAdvertsByFilterRequest request) {
        List<BiddingAdvert> filteredList = biddingAdvertRepository.findByCriteria(request.getCategoryId(), request.getVerificationStatus(), request.getAdStatus()).stream().map(new BiddingAdvertConverter()::convert).toList();
        return GetAllBiddingAdvertsByFilterResponse.builder().filteredBiddingAdList(filteredList).build();
    }

    @Override
    public SetAdvertStatusResponse setAdStatus(SetAdvertStatusRequest request) {
        Optional<BiddingAdEntity> adEntity = biddingAdvertRepository.findById(request.getId());
        if(adEntity.isEmpty()){
            throw new AdvertNotFoundException();
        }
        if(request.getVerificationStatus() == null && request.getAdvertStatus() == adEntity.get().getAdStatus()){
            return SetAdvertStatusResponse.builder().isUpdated(false).build();
        }
        if(request.getVerificationStatus() == adEntity.get().getVerification() && request.getAdvertStatus() == null){
            return SetAdvertStatusResponse.builder().isUpdated(false).build();
        }
        if(request.getVerificationStatus() == null && request.getAdvertStatus() == null){
            return SetAdvertStatusResponse.builder().isUpdated(false).build();
        }
        if(adEntity.get().getVerification() == request.getVerificationStatus() && adEntity.get().getAdStatus() == request.getAdvertStatus()){
            return SetAdvertStatusResponse.builder().isUpdated(false).build();
        }

        int numberOfUpdatedProperties = biddingAdvertRepository.updateAdStatus(request.getId(), request.getVerificationStatus(), request.getAdvertStatus());
        if(numberOfUpdatedProperties > 0){
            return SetAdvertStatusResponse.builder().isUpdated(true).build();
        }
        return SetAdvertStatusResponse.builder().isUpdated(false).build();
    }

    @Override
    public GetPersonalBiddingAdsResponse getPersonalBiddingAdverts(GetPersonalBiddingAdsRequest request) {
        Optional<AccountEntity> userAccountEntity = accountRepository.findById(request.getId());
        if(!userAccountEntity.isPresent()){
            throw new UserNotFoundException();
        }

        Account account = AccountConverter.convertToAccount(userAccountEntity.get());
        List<BiddingAdEntity> bidAdEntities = biddingAdvertRepository.findAdsByHighestBidderAndStatus(request.getId(), true);
        List<BiddingAdvert> bidAds = bidAdEntities.stream().map(new BiddingAdvertConverter()::convert).toList();
        return GetPersonalBiddingAdsResponse.builder().personalBiddingAdsList(bidAds).build();
    }

    public BiddingAdEntity saveAdvert(CreateBiddingAdvertRequest request){
        BiddingAdEntity biddingAdvertEntity = BiddingAdEntity.builder()
                .title(request.getTitle())
                .productDescription(request.getProductDescription())
                .category(request.getCategory())
                .advertiser(request.getAdvertiser())
                .lowestBidAmountAllowed(request.getLowestBidAmountAllowed())
                .currentHighestBid(0)
                .initialBidPrice(request.getInitialBidPrice())
                .binPrice(request.getBinPrice())
                .bidExpirationDate(request.getBidExpirationDate())
                .build();
        return biddingAdvertRepository.save(biddingAdvertEntity);
    }

}


