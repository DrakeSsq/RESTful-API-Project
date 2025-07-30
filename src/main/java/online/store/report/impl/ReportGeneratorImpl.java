package online.store.report.impl;

import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.store.report.ReportGenerator;
import online.store.report.dto.ReportDataDto;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

import static online.store.util.LogMessageUtil.ERROR_CLOSING_CSV;


@Slf4j
@Component
@RequiredArgsConstructor
public class ReportGeneratorImpl implements ReportGenerator {

    private final static String REPORT_NAME = "report.csv";
    private CSVWriter writer;

    {
        try {
            initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws IOException {
        if (writer!=null) {
            try {
                writer.close();
            } catch (IOException e) {
                log.info(ERROR_CLOSING_CSV, e.getMessage());
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

        try (FileWriter fileWriter = new FileWriter(REPORT_NAME, true)) {
            fileWriter.write(dto.getId().toString() + ", ");
            fileWriter.write(dto.getOldPrice().toString() + ", ");
            fileWriter.write(dto.getNewPrice().toString() + "\n");
        }
    }

    public void clearData() {
        try {
            initialize();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
