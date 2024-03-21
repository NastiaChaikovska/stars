package com.identification.stars;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;
import static com.mongodb.client.model.Updates.set;

@Service
public class StarsCatalogue {
    private final MongoCollection<Document> collection;

    public StarsCatalogue(MongoDatabase database) {
        this.collection = database.getCollection("stars");
    }

    public Document findNearestStarByCoordinates(double ra, double dec) {
        Document query = new Document("location",
                new Document("$near", new Document("$geometry",
                        new Document("type", "Point").append("coordinates", List.of(ra, dec)))));
        return collection.find(query).first();
    }

    public double findMaxMagStar() {
        Document maxMagDoc = collection.find().sort(orderBy(ascending("mag"))).first();
        assert maxMagDoc != null;
        return maxMagDoc.getDouble("mag");
    }

    public void calculateNewMag() {
        double maxMag = findMaxMagStar();
        for (Document currStar : collection.find()) {
            double newMag = -currStar.getDouble("mag") + maxMag + 3;
            collection.updateOne(eq("_id", currStar.get("_id")), set("new_mag", newMag));
        }
    }

    public List<Object[]> getStarsToExplore(double latitude, double longitude) {
//        calculateNewMag();
        List<Object[]> resultStars = new ArrayList<>();
        Document centralStar = findNearestStarByCoordinates(latitude, longitude);
        if (centralStar != null && centralStar.containsKey("location") && centralStar.get("location") instanceof Document) {
            Document location = (Document) centralStar.get("location");
            if (location.containsKey("coordinates") && location.get("coordinates") instanceof List) {
                List<?> coordinates = (List<?>) location.get("coordinates");
                if (coordinates.size() == 2 && coordinates.get(0) instanceof Double && coordinates.get(1) instanceof Double) {
                    Double centralStarRa = (Double) coordinates.get(0);
                    Double centralStarDec = (Double) coordinates.get(1);

                    for (Document currStar : collection.find()) {
                        Document starLocation = (Document) currStar.get("location");
                        if (starLocation != null && starLocation.containsKey("coordinates") && starLocation.get("coordinates") instanceof List) {
                            List<?> starCoordinates = (List<?>) starLocation.get("coordinates");
                            if (starCoordinates.size() == 2 && starCoordinates.get(0) instanceof Double && starCoordinates.get(1) instanceof Double) {
                                Double deltaRa = (Double) starCoordinates.get(0) - centralStarRa;
                                Double deltaDec = (Double) starCoordinates.get(1) - centralStarDec;
                                Double newMag = currStar.getDouble("new_mag");
                                resultStars.add(new Object[]{new Double[]{deltaRa, deltaDec}, newMag});
                            }
                        }
                    }
                }
            }
        }
        return resultStars;
    }

//    public List<Object[]> getStarsToExplore(double latitude, double longitude) {
//        List<Object[]> resultStars = new ArrayList<>();
//        Document centralStar = findNearestStarByCoordinates(latitude, longitude);
////        List<Double> centralStarCoordinates = (List<Double>) centralStar.get("location.coordinates");
//        List<Double> centralStarCoordinates = (List<Double>) centralStar.get("location");
//
//        if (centralStarCoordinates != null) { // Add null check here
//            double centralStarRa = centralStarCoordinates.get(0);
//            double centralStarDec = centralStarCoordinates.get(1);
//
//            for (Document currStar : collection.find()) {
//                List<Double> currStarCoordinates = (List<Double>) currStar.get("location.coordinates");
//                if (currStarCoordinates != null && currStarCoordinates.size() == 2) { // Add null and size check
//                    double deltaRa = currStarCoordinates.get(0) - centralStarRa;
//                    double deltaDec = currStarCoordinates.get(1) - centralStarDec;
//                    double newMag = currStar.getDouble("new_mag");
//                    resultStars.add(new Object[]{new double[]{deltaRa, deltaDec}, newMag});
//                }
//            }
//        }
//
//        return resultStars;
//    }
}
