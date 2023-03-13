import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlightStats {

    public static void main(String[] args) {

        try {
            // Чтение файла tickets.json в виде строки
            String content = new String(Files.readAllBytes(Paths.get("tickets.json")));

            // Преобразование строки в JSON-объект
            JSONObject json = new JSONObject(content);

            // Получение массива билетов из JSON-объекта
            JSONArray tickets = json.getJSONArray("tickets");

            // Вычисление среднего времени полета между городами Владивосток и Тель-Авив
            List<Integer> flightTimes = new ArrayList<>();
            for (int i = 0; i < tickets.length(); i++) {
                JSONObject ticket = tickets.getJSONObject(i);
                if (ticket.getString("origin").equals("VVO") && ticket.getString("destination").equals("TLV")) {
                    int departureTime = getTimeInMinutes(ticket.getString("departure_time"));
                    int arrivalTime = getTimeInMinutes(ticket.getString("arrival_time"));
                    int flightTime = arrivalTime - departureTime;
                    flightTimes.add(flightTime);
                }
            }
            double averageFlightTime = flightTimes.stream().mapToDouble(a -> a).average().orElse(0.0);

            // Вычисление 90-го процентиля времени полета между городами Владивосток и Тель-Авив
            Percentile percentile = new Percentile();
            percentile.setData(flightTimes.stream().mapToDouble(a -> a).toArray());
            double percentile90 = percentile.evaluate(90.0);

            // Вывод результатов на экран
            System.out.println("Среднее время полета между городами Владивосток и Тель-Авив: " + formatTime(averageFlightTime));
            System.out.println("90-й процентиль времени полета между городами Владивосток и Тель-Авив: " + formatTime(percentile90));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    // Метод для преобразования времени в формате "чч:мм" в количество минут
    private static int getTimeInMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    // Метод для форматирования времени в минутах в формат "чч чм"
    private static String formatTime(double timeInMinutes) {
        int hours = (int) (timeInMinutes / 60);
        int minutes = (int) (timeInMinutes % 60);
        return String.format("%02d ч %02d мин", hours, minutes);
    }
}