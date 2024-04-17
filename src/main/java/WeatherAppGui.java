import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    public WeatherAppGui() {
        //set up GUI and set title
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

        // change the cursor to a hand cursor when hovering over this button
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13,47,45);
        add(searchButton);

        //weather image
        JLabel weatherConditionImage = new JLabel(new ImageIcon("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125,450,217);
        add(weatherConditionImage);

        // temperature text
        JLabel temperatureText = new JLabel("Temperature");
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
        JLabel humidityText = new JLabel("Humidity");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // windSpeed image
        JLabel windSpeedImage = new JLabel(new ImageIcon("src/assets/windspeed.png"));
        windSpeedImage.setBounds(220, 500,74,66);
        add(windSpeedImage);


        // windSpeed text
        JLabel windSpeedText = new JLabel("Windspeed");
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windSpeedText.setBounds(310, 500, 85, 55);
        add(windSpeedText);
    }

    // read the image file from the path given
    private ImageIcon loadImage(String resourcePath) {
        try{
            //read the image file from the path given
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
