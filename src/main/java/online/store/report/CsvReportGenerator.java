package online.store.report;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import online.store.report.dto.ReportDataDto;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static online.store.util.LogMessageUtil.ERROR_CREATING_CSV;

@Slf4j
@Component
public class CsvReportGenerator implements ReportGenerator{

    private final List<ReportDataDto> listData = new ArrayList<>();

    @Override
    public void generateReport() {
        try {
            CSVWriter writer;
            writer = new CSVWriter(new FileWriter("report.csv"), ',', '"', '"', "\n");

            writer.writeNext(new String[]{"id", "oldPrice", "newPrice"});

            for (ReportDataDto data : listData) {
                writer.writeNext(new String[]{
                                data.getId().toString(),
                                data.getOldPrice().toString(),
                                data.getNewPrice().toString()
                        }
                );
            }

            listData.clear();

        } catch (IOException e) {
            log.info(ERROR_CREATING_CSV, e.getMessage());
        }
    }

    @Override
    public void addData(ReportDataDto reportDataDto) {
        listData.add(reportDataDto);
    }
}
