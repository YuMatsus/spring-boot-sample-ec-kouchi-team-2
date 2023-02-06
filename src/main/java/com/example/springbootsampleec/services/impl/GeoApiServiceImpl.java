package com.example.springbootsampleec.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.services.GeoApiService;
import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

@Service
public class GeoApiServiceImpl implements GeoApiService {

    // 住所から座標を取得
    @Transactional
    @Override
    public LatLng getLatLng(String address) {
        GeoApiContext context = getGeoApiContext();
        Geometry geometry = new Geometry();      
        try {
            GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
            geometry = results[0].geometry;
        } catch (Exception E){     
            return null;       
        } finally {
            context.shutdown();
        }
        LatLng latLng = new LatLng(geometry.location.lat, geometry.location.lng);
        return latLng;
    }

    // 座標間の距離情報を取得
    @Transactional
    @Override
    public DirectionsResult getDirectionsResult(LatLng originLatLng, LatLng destinationLatLng) {
        GeoApiContext context = getGeoApiContext();
        DirectionsResult result = new DirectionsResult();
        try {
            result = DirectionsApi.newRequest(context)
                .origin(originLatLng)
                .destination(destinationLatLng)
                .mode(TravelMode.DRIVING)
                .await();
        } catch (Exception E){     
            return null;       
        } finally {
            context.shutdown();
        }
        return result;
    }

    // 複数の座標間の距離情報を取得
    @Transactional
    @Override
    public DistanceMatrix getDistanceMatrix(LatLng[] originLatLngs, LatLng[] destinationLatLngs) {
        GeoApiContext context = getGeoApiContext();
        DistanceMatrix result = new DistanceMatrix(null, null, null);
        try {
            result = DistanceMatrixApi.newRequest(context)
                .origins(originLatLngs)
                .destinations(destinationLatLngs)
                .mode(TravelMode.DRIVING)
                .await();
        } catch (Exception E){     
            return null;       
        } finally {
            context.shutdown();
        }
        return result;
    }
    
    // 店舗-ユーザー間の所要時間(秒)を取得
    @Override
    public long getRequiredTimeStoreToUser(Store store, User user) {
        // ユーザーに座標が登録されていない場合は1800秒(=30分)を返す。
        if(user.getLat() == 0 || user.getLng() == 0) return 1800;
        // ユーザーの座標を取得
        LatLng userLatLng = new LatLng(user.getLat(), user.getLng());
        // 店舗に座標が登録されていない場合は1800秒(=30分)を返す。
        if(store.getLat() == 0 || store.getLng() == 0) return 1800;
        // 店舗の座標を取得
        LatLng storeLatLng = new LatLng(store.getLat(), store.getLng());
        // 経路情報を取得
        DirectionsResult directionsResult = getDirectionsResult(userLatLng, storeLatLng);
        // 取得できない場合は1800秒(=30分)を返す。
        if(directionsResult == null) return 1800;

        return directionsResult.routes[0].legs[0].duration.inSeconds;
    }

    // Google Geo APIs認証
    private GeoApiContext getGeoApiContext() {
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("API_KEY")
            .build();
        return context;
    }
    
}
