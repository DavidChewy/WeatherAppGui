import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {

    private JSONObject weatherData;

    public WeatherAppGui() {
        // set up GUI and set title
        super("Weather App");

        // configure gui to end the program's process once it has been closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(450, 650);

        setLocationRelativeTo(null);

        setLayout(null);

        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        //search field
        JTextField searchTextField = new JTextField(15);

        // set the location and size of our component
        searchTextField.setBounds(15, 15, 351,45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));


        // weather image
        JLabel weatherConditionImage = new JLabel(new ImageIcon("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125,450,217);
        add(weatherConditionImage);

        // temperature text
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setBounds(0, 350,450,54);

        // center the text
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);


        // weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setFont(new Font("Dialog", Font.BOLD, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        weatherConditionDesc.setBounds(0, 405,450,36);
        add(weatherConditionDesc);

        // humidity image
        JLabel humidityImage = new JLabel(new ImageIcon("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500,74,66);
        add(humidityImage);

        // humidity text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 100, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // windSpeed image
        JLabel windSpeedImage = new JLabel(new ImageIcon("src/assets/windspeed.png"));
        windSpeedImage.setBounds(220, 500,74,66);
        add(windSpeedImage);


        // windSpeed text
        JLabel windSpeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windSpeedText.setBounds(300, 500, 85, 55);
        add(windSpeedText);

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13,47,45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput =searchTextField.getText();

                // validate input - remove whitespace to ensure non-empty text
                if(userInput.replaceAll("\\s", "").length() <=0){
                    return;
                };
                // retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);

                // update weather image

                String weatherCondition = (String) weatherData.get("weather_condition");

                //depending on the condition, we will update the weather image that corresponds with the condition

                switch(weatherCondition){
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;

                }
                // update temperate text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + "C");

                // update weather condition text
                weatherConditionDesc.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                // update windspeed text
                double windspeed = (double) weatherData.get("wind_speed");
                windSpeedText.setText("<html><b>Windspeed " + windspeed + "km/h</html>");

            }

        });
        add(searchButton);

    }

    // read the image file from the path given
    private ImageIcon loadImage(String resourcePath) {
        try{
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));
            // returns an image icon so that our component can render it
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Couldn't load image");
        return null;

    }

}
