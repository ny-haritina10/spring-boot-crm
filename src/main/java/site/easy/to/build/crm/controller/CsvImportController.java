package site.easy.to.build.crm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.service.csv.CsvImportService;

@Controller
@RequestMapping("/csv-import")
public class CsvImportController {

    @Autowired
    private CsvImportService csvImportService;

    @GetMapping
    public String showImportForm() {
        return "csv-import/import-form";
    }

    @PostMapping
    public String processImport(@RequestParam("customersFile") MultipartFile customersFile,
                               @RequestParam("itemsFile") MultipartFile itemsFile,
                               Model model) {
        // Check if files were uploaded
        if (customersFile.isEmpty() || itemsFile.isEmpty()) {
            model.addAttribute("error", "Please select both CSV files.");
            return "csv-import/import-form";
        }

        // Prepare files map
        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("customersFile", customersFile);
        fileMap.put("itemsFile", itemsFile);

        // Process the import
        List<CsvImportService.ImportError> errors = csvImportService.importMultipleCsvFiles(fileMap);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "csv-import/import-form";
        }

        model.addAttribute("success", "CSV data imported successfully.");
        return "csv-import/import-form";
    }
}