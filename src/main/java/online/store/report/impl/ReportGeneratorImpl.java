package online.store.report.impl;

import com.opencsv.CSVWriter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

import static online.store.util.LogMessageUtil.ERROR_CLEAR_DATA;
import static online.store.util.LogMessageUtil.ERROR_CLOSING_CSV;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReportGeneratorImpl implements ReportGenerator {

    @Value("${report.name}")
    private String REPORT_NAME;
    private CSVWriter writer;

    @PostConstruct
    public void init() throws IOException {
        initialize();
    }

    public void initialize() throws IOException {
        if (writer!=null) {
            try {
                writer.close();
            } catch (IOException e) {
                log.error(ERROR_CLOSING_CSV, e.getMessage());
            }
        }

        writer = new CSVWriter(new FileWriter(REPORT_NAME));
        writer.writeNext(new String[]{"id", "old_price", "new_price"});
        writer.flush();
    }

    public void writeData(ReportDataDto dto) throws IOException {

        if (writer==null) {
            initialize();
        }

        writer.writeNext(new String[]{
                dto.getId().toString(),
                dto.getOldPrice().toString(),
                dto.getNewPrice().toString()
        });
    }

    public void clearData() {
        try {
            initialize();
        } catch (IOException e) {
            log.error(ERROR_CLEAR_DATA, e.getMessage());
            throw new RuntimeException();
        }
    }
}
