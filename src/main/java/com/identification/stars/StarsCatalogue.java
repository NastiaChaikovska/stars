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

    public Document findNearestStarByCoordinates(double dec, double ra) {
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

    public StarsExplore getStarsToExplore(double latitude, double longitude) {
//        calculateNewMag();
        List<Object[]> resultStars = new ArrayList<>();
        Document centralStar = findNearestStarByCoordinates(latitude, longitude);
        Integer centralStarId = Integer.parseInt(centralStar.getString("_id"));
        Double centralStarRa = null;
        Double centralStarDec = null;
        if (centralStar != null && centralStar.containsKey("location") && centralStar.get("location") instanceof Document) {
            Document location = (Document) centralStar.get("location");
            if (location.containsKey("coordinates") && location.get("coordinates") instanceof List) {
                List<?> coordinates = (List<?>) location.get("coordinates");
                if (coordinates.size() == 2 && coordinates.get(0) instanceof Double && coordinates.get(1) instanceof Double) {
                    centralStarRa = (Double) coordinates.get(0);
                    centralStarDec = (Double) coordinates.get(1);

                    for (Document currStar : collection.find()) {
                        Document starLocation = (Document) currStar.get("location");
                        if (starLocation != null && starLocation.containsKey("coordinates") && starLocation.get("coordinates") instanceof List) {
                            List<?> starCoordinates = (List<?>) starLocation.get("coordinates");
                            if (starCoordinates.size() == 2 && starCoordinates.get(0) instanceof Double && starCoordinates.get(1) instanceof Double) {
                                Double newMag = currStar.getDouble("new_mag");
                                String id = currStar.getString("_id");
                                Integer idInteger = Integer.parseInt(id);
                                resultStars.add(
                                        new Object[]{new Double[]{(Double) starCoordinates.get(0), (Double) starCoordinates.get(1)},
                                        newMag, idInteger}
                                ); //, new Double[]{(Double) starCoordinates.get(0), (Double) starCoordinates.get(1)}, mag});
                            }
                        }
                    }
                }
            }
        }
        return new StarsExplore(resultStars, centralStarId, centralStarRa, centralStarDec);
    }

    // List<Double>
    public StarData getStarInfo(String id) {
        Document starDocument = collection.find(eq("_id", id)).first();

        if (starDocument != null) {
            double mag = starDocument.getDouble("mag");

            Document location = starDocument.get("location", Document.class);
            List<Double> coordinates = location.getList("coordinates", Double.class);
            double ra = coordinates.get(0);
            double dec = coordinates.get(1);
            return new StarData(mag, ra, dec);
        } else {
            return null;
        }
    }
}