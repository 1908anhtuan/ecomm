package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.*;

public interface RegularAdvertUseCases {
    CreateRegularAdvertResponse createRegularAdvert(CreateRegularAdvertRequest request);
    GetAllRegularAdvertsResponse getAll();
    Boolean deleteRegularAdvert(Long id);
    GetRegularAdvertByIdResponse getRegularAdvertDetails(Long id);
    BuyRegularAdvertResponse buyRegularAdvert(Long id,BuyRegularAdvertRequest request);
    GetAllRegularAdvertsByFilterResponse getAllFilteredRegularAds(GetAllRegularAdvertsByFilterRequest request);
    SetAdvertStatusResponse setAdStatus(SetAdvertStatusRequest request);
    GetPersonalRegularAdsResponse getPersonalRegularAdverts(GetPersonalRegularAdsRequest request);
}
