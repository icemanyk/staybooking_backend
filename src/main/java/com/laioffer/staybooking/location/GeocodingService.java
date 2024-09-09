package com.laioffer.staybooking.location;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.laioffer.staybooking.model.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocodingService {

    private final GeoApiContext context;

    public GeocodingService(GeoApiContext context) {
        this.context = context;
    }

    public GeoPoint getGeoPoint(String address) {
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0]; // await相当于执行，但是要拿到执行结果的array，再执行
            if (result.partialMatch) { // 如果是partialMatch，意味着是模糊查找，就会丢出异常
                throw new InvalidAddressException();
            }
            return new GeoPoint(result.geometry.location.lat, result.geometry.location.lng);
        } catch (IOException | ApiException | InterruptedException e) {
            throw new GeocodingException();
        }
    }
}
