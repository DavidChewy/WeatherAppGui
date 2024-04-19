// retrieve weather data from API - this backend logic will fetch the latest weather
// data from the external API and return it.
// the GUI will display the data to the user

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);

        // extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // build API request URL with location coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?"+ "latitude=" + latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Europe%2FLondon";

        try{
            // call api and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            //check for response status
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }
            // store resulting json data
           StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }

            // close scanner
            scanner.close();

            // close connection
            conn.disconnect();

            // parse through our data
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(resultJson.toString());

            // retrieve hourly data
            JSONObject hourly = (JSONObject) jsonObject.get("hourly");

            // we want to get current hour's data
            // so we need to index our time
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // get weather code
            JSONArray weathercode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long)weathercode.get(index));


            // get humidity data
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // get windspeed
            JSONArray windSpeedData =  (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windSpeedData.get(index);

            // build the weather json data object that we are going to access in our frontend
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("wind_speed", windspeed);

            return weatherData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

}
    public static JSONArray getLocationData(String locationName){
        // replace any whitespace in location name to + to adhere to API's request format
        locationName = locationName.replaceAll(" ", "+");

        // build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName + "&count=10&language=en&format=json";

        try{
            // call api and get a message
            HttpURLConnection conn =  fetchApiResponse(urlString);

            // check response status
            // 200 means successful
            assert conn != null;
            if(conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }else{
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while(scanner.hasNextLine()){
                    resultJson.append(scanner.nextLine());
                }
                // close the scanner
                scanner.close();

                // close url connection
                conn.disconnect();

                // parse the JSON string into a JSON obj
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(resultJson.toString());

                // get the list of location data the API generated from the location name
                JSONArray locationData = (JSONArray) jsonObject.get("results");
                return locationData;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        // couldn't find location
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) throws Exception{
        // attempt to create a connection
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;
        }catch (Exception e){
            e.printStackTrace();
        }

        // could not make a connection

        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();


        //iterate through the time list and see which one matches our current time
        for(int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equals(currentTime)){
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        // format the date to be specific with the API

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    private static String convertWeatherCode(long weathercode){
        String weatherCondition = "";
        if(weathercode == 0L){
            weatherCondition = "Clear Sky";
        }else if(weathercode >0L && weathercode <= 3L){
            weatherCondition = "Cloudy";
        }else if((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L)){

            weatherCondition = "Rain";
        }else if(weathercode >= 71L && weathercode <= 77L){
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}
