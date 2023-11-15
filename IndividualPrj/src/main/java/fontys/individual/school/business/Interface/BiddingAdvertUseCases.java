package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.BiddingAdvert;
import fontys.individual.school.domain.HttpResponseRequest.*;

import java.util.Optional;

public interface BiddingAdvertUseCases {
    CreateBiddingAdvertResponse createBiddingAdvert(CreateBiddingAdvertRequest request);
    GetAllBiddingAdvertsResponse getAll();
    Boolean deleteBiddingAdvert(Long id);
    Optional<BiddingAdvert> getBiddingAdvertDetails(Long id);
    BuyNowBiddingAdvertResponse buyBiddingAdvert(Long id, BuyNowBiddingAdvertRequest request);
    BidResponse bidAdvert(Long id, BidRequest request);
    GetAllBiddingAdvertsByFilterResponse getAllFilteredBiddingAds(GetAllBiddingAdvertsByFilterRequest request);
    SetAdvertStatusResponse setAdStatus(SetAdvertStatusRequest request);
    GetPersonalBiddingAdsResponse getPersonalBiddingAdverts(GetPersonalBiddingAdsRequest request);


}
