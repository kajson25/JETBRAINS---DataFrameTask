# **DataFrame Visualization Solution**

## **Overview**
This solution enhances the default `DataFrame` visualization methods by addressing their limitations and providing an interactive, user-friendly tool built with **Jetpack Compose for Desktop**. It supports hierarchical data, numerical analysis, and export capabilities, making it ideal for exploring and reporting complex datasets.

## **Problems with Existing Methods**
1. **`toString` and `schema().toString`:**
    - Limited to flat, textual representations.
    - No support for hierarchical or complex data types like images.
2. **`toHTML`:**
    - Static output with no interactivity.
    - Cannot handle nested structures effectively.
3. **Kotlin Notebook Table:**
    - Restricted to notebook environments.
    - Lacks advanced features like exporting or custom rendering.

## **Solution Highlights**
1. **Hierarchical Data Rendering:**
    - Interactive UI for nested structures (`Map`, `List`, `BufferedImage`), with expandable/collapsible views.
2. **Schema View:**
    - Displays column names, types, and nullability.
3. **Numerical Analysis:**
    - Summary statistics (mean, median, variance) and histograms for numerical columns.
4. **File Handling:**
    - Supports JSON, CSV, Excel, and SQL input.
5. **Exporting:**
    - Export visualizations to HTML or PDF for sharing or reporting.

## **Key Benefits**
- **Interactivity:** Dynamic exploration of hierarchical data and long strings.
- **Advanced Insights:** Numerical analysis for datasets with many numerical columns.
- **Shareability:** Combines interactivity with exportable static formats.
- **Flexibility:** Works seamlessly with multiple file formats.

## **Limitations**
- **Performance:** Rendering very large datasets can be slow.
- **Static Exports:** HTML/PDF exports are static and lose interactivity.

## **Future Improvements**
- Add pagination or virtualization for large datasets.
- Support interactive HTML exports and advanced analytics.

This solution bridges the gap between exploration, analysis, and reporting, making `DataFrame` visualization more powerful and accessible.

## **How to Run**

### **Prerequisites**
- Ensure you have **Kotlin** and **Gradle** installed.
- Add the following dependencies to your `build.gradle.kts` file:
  ```kotlin
  dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:dataframe:0.11.0")
    implementation("org.jetbrains.kotlinx:dataframe-excel:0.10.0")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("com.itextpdf:html2pdf:4.0.4")
    implementation("com.zaxxer:HikariCP:5.0.1")
}

## **Steps to Run**

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>

2. Build and run the application using Gradle:
   ```bash
   ./gradlew run

## **How to Use**

1. **Open File:**
   - Click the **"Open File"** button to load a file from your filesystem.
   - Supported formats include:
      - JSON
      - CSV
      - Excel
      - SQL queries (stored in `.sql` files) - not that file should contain database path and query to execute

2. **Schema View:**
   - Click the **"Show Schema"** button to display the `DataFrame` schema:
      - Column names.
      - Data types.
      - Nullability of columns.
   - Click **"Hide Schema"** to return to the main view.

3. **Numerical Analysis:**
   - Click the **"Show Analysis"** button to view:
      - Summary statistics for numerical columns, including:
         - Mean.
         - Median.
         - Variance.
      - Histograms for visualizing the distribution of numerical data.
   - Click **"Hide Analysis"** to return to the main view.

4. **Export Visualizations:**
   - Use the **"Export to HTML"** button to save the current visualization as a static HTML file.
   - Use the **"Export to PDF"** button to save the current visualization as a PDF report.

5. **Interactive Data Exploration:**
   - Explore hierarchical data dynamically using expandable/collapsible views for nested structures (`Map`, `List`, or nested `DataFrame`).
   - Long strings are truncated for readability but can be expanded by clicking on them.
   - Images (loaded via URLs or file paths) are displayed directly in the visualization.

## **Code Overview**

### **Key Features**
1. **`HierarchicalCardView`:**
   - Dynamically renders hierarchical data, including `Map`, `List`, and nested `DataFrame` structures.
   - Provides expandable/collapsible views for better navigation.
   - Handles complex data types:
      - Displays images stored as URLs or file paths.
      - Truncates long strings with the option to expand for full viewing.
      - Highlights null values for easy identification.

2. **`SchemaView`:**
   - Displays the `DataFrame` schema:
      - Column names.
      - Data types (e.g., `String`, `Int`, `Boolean`).
      - Nullability of columns.
   - Provides a quick overview of the dataset’s structure.

3. **`StatisticsView`:**
   - Calculates and displays numerical statistics for columns with numeric data types:
      - Mean.
      - Median.
      - Variance.
   - Visualizes distributions of numerical data using histograms for intuitive analysis.

4. **Exporting Visualizations:**
   - **HTML Export:** Saves the current `DataFrame` view as an interactive HTML table.
   - **PDF Export:** Saves the visualization as a static PDF report, leveraging the iText library.

5. **File Handling:**
   - Supports loading data from multiple file formats:
      - JSON.
      - CSV.
      - Excel.
      - SQL queries (stored in `.sql` files).
   - Default file chooser directory is set to `src/main/resources` for convenience.

---

### **Composable Components**
- **`App`:** The main composable that:
   - Manages the state of the application (e.g., loaded `DataFrame`, schema view, numerical analysis view).
   - Provides the user interface for file loading, exporting, and toggling between views.
- **`RenderDynamicData`:**
   - Recursively renders individual rows and columns of the `DataFrame`.
   - Handles special cases for images, long strings, null values, and nested structures.
- **`Histogram`:**
   - Generates a histogram to visualize the distribution of numerical column data.
   - Automatically adjusts the number of bins based on data range.

---

### **Extensibility**
This codebase is modular and designed for future enhancements:
- Add support for advanced visualizations (e.g., scatter plots, heatmaps).
- Implement pagination or virtualized rendering for large datasets.
- Include interactive features for numerical analysis (e.g., filter by range, drill-down views).
- Extend export functionality to support additional formats, such as Excel or CSV.

### **Contact**
- katarina.vucicevic25@gmail.com