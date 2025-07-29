package online.store.report;

import online.store.entity.Product;
import online.store.report.dto.ReportDataDto;


public interface ReportGenerator {

    void generateReport();

    void addData(ReportDataDto reportDataDto);
}
