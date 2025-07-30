package online.store.report;

import online.store.report.dto.ReportDataDto;

import java.io.IOException;


public interface ReportGenerator {

    void initialize() throws IOException;
    void writeData(ReportDataDto reportDataDto) throws IOException;
    void clearData();
}
