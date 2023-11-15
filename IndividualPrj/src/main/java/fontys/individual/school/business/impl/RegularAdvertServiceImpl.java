package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.RegularAdvertUseCases;
import fontys.individual.school.business.exception.*;
import fontys.individual.school.business.impl.Converter.AccountConverter;
import fontys.individual.school.business.impl.Converter.RegularAdvertConverter;
import fontys.individual.school.domain.Account;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.RegularAdvert;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.RegularAdvertRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.RegularAdEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegularAdvertServiceImpl implements RegularAdvertUseCases {
    private final RegularAdvertRepository regularAdvertRepository;
    private final AccountRepository accountRepository;
    @Override
    public CreateRegularAdvertResponse createRegularAdvert(CreateRegularAdvertRequest request) {

        if(request.getPrice() < 5){
            throw new BadRequestException("Minimum price is 5");
        }

        if(request.getTitle().length() < 5){
            throw new BadRequestException("Title is too short!");
        }
        if(request.getProductDescription().length() < 20){
            throw  new BadRequestException("Description is too short!");
        }
        RegularAdEntity savedRegularAdvert = saveAdvert(request);

        return CreateRegularAdvertResponse.builder()
                .regularAdvert(RegularAdvertConverter.builder().build().convert(savedRegularAdvert))
                .build();

    }

    @Override
    public GetAllRegularAdvertsResponse getAll() {
        List<RegularAdEntity> results = regularAdvertRepository.findAll();

        List<RegularAdvert> regularAdverts = results
                .stream()
                .map(RegularAdvertConverter.builder().build()::convert)
                .toList();

        return GetAllRegularAdvertsResponse.builder()
                .regularAdvertList(regularAdverts)
                .build();
    }

    @Override
    public Boolean deleteRegularAdvert(Long id) {
        Optional<RegularAdEntity> advertEntity = regularAdvertRepository.findById(id);
        if (advertEntity.isPresent()) {
            try {
                regularAdvertRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public GetRegularAdvertByIdResponse getRegularAdvertDetails(Long id) {
        Optional<RegularAdEntity> advertEntity = regularAdvertRepository.findById(id);
        if (!advertEntity.isPresent()) {
            throw new AdvertNotFoundException();
        }
        regularAdvertRepository.incrementViewCount(id);
        RegularAdvertConverter converter = RegularAdvertConverter.builder().build();
        RegularAdvert regularAdvert = converter.convert(advertEntity.get());
        return GetRegularAdvertByIdResponse.builder().regularAdvert(regularAdvert).build();
    }

    @Override
    public BuyRegularAdvertResponse buyRegularAdvert(Long advertId, BuyRegularAdvertRequest request) {
        RegularAdvertConverter regularAdvertConverter = new RegularAdvertConverter();
        Optional<RegularAdEntity> advertEntity = regularAdvertRepository.findById(advertId);
        if(!advertEntity.isPresent()){
            throw new AdvertNotFoundException();
        }

        RegularAdvert advert = new RegularAdvertConverter().convert(advertEntity.get());
        Optional<AccountEntity> userAccountEntity = accountRepository.findById(request.getUserId());
        if(!userAccountEntity.isPresent()){
            throw new UserNotFoundException();
        }

        Account account = AccountConverter.convertToAccount(userAccountEntity.get());
        if(account.getBalance() < advert.getPrice()){
            throw new InsufficientBalanceException();
        }

        account.setBalance(account.getBalance() - advert.getPrice());


        if(!advert.buyInstantly(account)){
            throw new ResourceNotAvailableException();
        }
        AccountEntity toBeSavedAccountEntity = AccountConverter.convertToAccountEntity(account);
        accountRepository.save(toBeSavedAccountEntity);
        RegularAdEntity toBeSavedAdvertEntity = new RegularAdvertConverter().convertToEntity(advert);
        RegularAdEntity savedAdvert = regularAdvertRepository.save(toBeSavedAdvertEntity);
        return BuyRegularAdvertResponse.builder().regularAdvert(regularAdvertConverter.convert(savedAdvert)).build();

    }

    @Override
    public GetAllRegularAdvertsByFilterResponse getAllFilteredRegularAds(GetAllRegularAdvertsByFilterRequest request) {
        List<RegularAdvert> filteredList = regularAdvertRepository.findByCriteria(request.getCategoryId(), request.getVerificationStatus(), request.getAdStatus()).stream().map(new RegularAdvertConverter()::convert).toList();
        return GetAllRegularAdvertsByFilterResponse.builder().filteredRegularAdList(filteredList).build();
    }

    @Override
    public SetAdvertStatusResponse setAdStatus(SetAdvertStatusRequest request) {
         Optional<RegularAdEntity> adEntity = regularAdvertRepository.findById(request.getId());
         if(adEntity.isEmpty()){
             throw new AdvertNotFoundException();
         }
        if(request.getVerificationStatus() == null && request.getAdvertStatus() == adEntity.get().getAdStatus()){
            return SetAdvertStatusResponse.builder().isUpdated(false).build();
        }
        if(request.getVerificationStatus() == adEntity.get().getVerification() && request.getAdvertStatus() == null){
            return SetAdvertStatusResponse.builder().isUpdated(false).build();
        }
         if(adEntity.get().getVerification() == request.getVerificationStatus() && adEntity.get().getAdStatus() == request.getAdvertStatus()){
             return SetAdvertStatusResponse.builder().isUpdated(false).build();
         }
         int numberOfUpdatedProperties = regularAdvertRepository.updateAdStatus(request.getId(), request.getVerificationStatus(), request.getAdvertStatus());
         if(numberOfUpdatedProperties > 0){
             return SetAdvertStatusResponse.builder().isUpdated(true).build();
         }
         return SetAdvertStatusResponse.builder().isUpdated(false).build();
    }

    @Override
    public GetPersonalRegularAdsResponse getPersonalRegularAdverts(GetPersonalRegularAdsRequest request) {
        Optional<AccountEntity> userAccountEntity = accountRepository.findById(request.getId());
        if(!userAccountEntity.isPresent()){
            throw new UserNotFoundException();
        }

        Account account = AccountConverter.convertToAccount(userAccountEntity.get());
        List<RegularAdEntity> regAdEntities = regularAdvertRepository.findByBuyerId(request.getId());
        List<RegularAdvert> regAds = regAdEntities.stream().map(new RegularAdvertConverter()::convert).toList();
        return GetPersonalRegularAdsResponse.builder().personalRegularAdsList(regAds).build();
    }


    public RegularAdEntity saveAdvert(CreateRegularAdvertRequest request){
        RegularAdEntity regularAdvertEntity = RegularAdEntity.builder()
                .title(request.getTitle())
                .productDescription(request.getProductDescription())
                .category(request.getCategory())
                .advertiser(request.getAdvertiser())
                .price(request.getPrice())
                .condition(request.getCondition())
                .build();
        return regularAdvertRepository.save(regularAdvertEntity);
    }


}
