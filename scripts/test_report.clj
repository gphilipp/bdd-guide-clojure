(import '(java.io File)
  '(net.masterthought.cucumber Configuration ReportBuilder))

(let [report-output-dir (File. "target")
      json-files ["target/cucumber-html-reports/acceptance-test-results.json"]
      configuration (Configuration. report-output-dir "calculator")
      builder (ReportBuilder. json-files configuration)]
  (.generateReports builder))