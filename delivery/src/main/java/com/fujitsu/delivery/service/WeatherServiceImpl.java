package com.fujitsu.delivery.service;

import com.fujitsu.delivery.constants.City;
import com.fujitsu.delivery.model.Weather;
import com.fujitsu.delivery.constants.WeatherPhenomenon;
import com.fujitsu.delivery.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepo;

    public WeatherServiceImpl(WeatherRepository weatherRepo) {
        this.weatherRepo = weatherRepo;
    }

    /**
     * Adds weather data to the database
     *
     * @param weather - weather data
     */
    @Override
    public void addWeather(Weather weather) {
        weatherRepo.save(weather);
    }

    /**
     * Finds the latest weather data for that city
     *
     * @param city - city name
     * @return - weather data
     */
    @Override
    public Weather findLatestWeatherByCity(City city) {
        return weatherRepo.findTop1WeatherByNameOrderByTimestampDesc(city);
    }

    /**
     * Updates weather data from the Ilmateenistus XML
     */
    @Override
    public void updateWeatherData() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");

        NodeList nList = doc.getElementsByTagName("station");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String stationName = eElement.getElementsByTagName("name").item(0).getTextContent();
                if (stationName.equals("Tallinn-Harku") || stationName.equals("Pärnu") || stationName.equals("Tartu-Tõravere")) {
                    Weather weather = new Weather();

                    if (stationName.equals("Tallinn-Harku")) weather.setName(City.TALLINN);
                    else if (stationName.equals("Tartu-Tõravere")) weather.setName(City.TARTU);
                    else weather.setName(City.PÄRNU);

                    weather.setWindSpeed(Double.parseDouble(eElement.getElementsByTagName("windspeed").item(0).getTextContent()));
                    weather.setAirTemperature(Double.parseDouble(eElement.getElementsByTagName("airtemperature").item(0).getTextContent()));

                    String pheno = eElement.getElementsByTagName("phenomenon").item(0).getTextContent();
                    if (pheno.contains("snow") || pheno.contains("sleet"))
                        weather.setWeatherPhenomenon(WeatherPhenomenon.SNOWY);
                    else if (pheno.contains("rain") || pheno.contains("shower"))
                        weather.setWeatherPhenomenon(WeatherPhenomenon.RAINY);
                    else if (pheno.contains("glaze") || pheno.contains("hail") || pheno.contains("thunder"))
                        weather.setWeatherPhenomenon(WeatherPhenomenon.GLAZE);
                    else weather.setWeatherPhenomenon(WeatherPhenomenon.NONE);

                    weather.setWMOCode(Integer.parseInt(eElement.getElementsByTagName("wmocode").item(0).getTextContent()));
                    weather.setTimestamp(Timestamp.from(Instant.now()));
                    weatherRepo.save(weather);
                }
            }
        }
    }
}
