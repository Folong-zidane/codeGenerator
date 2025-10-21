package main

import (
	"bytes"
	"encoding/json"
	"flag"
	"fmt"
	"io"
	"net/http"
	"os"
)

type GenerateRequest struct {
	UmlContent     string `json:"umlContent"`
	PackageName    string `json:"packageName"`
	OutputPath     string `json:"outputPath"`
	GenerationType string `json:"generationType"`
	Framework      string `json:"framework"`
}

type GenerateResponse struct {
	Success    bool   `json:"success"`
	Message    string `json:"message"`
	ClassCount int    `json:"classCount"`
}

func main() {
	var (
		command     = flag.String("cmd", "generate", "Command: generate, validate")
		umlFile     = flag.String("uml", "", "UML diagram file (required)")
		outputPath  = flag.String("output", "", "Output directory (required)")
		framework   = flag.String("framework", "spring-boot", "Target framework")
		packageName = flag.String("package", "com.example", "Package name")
		apiURL      = flag.String("api-url", "http://localhost:8080", "API URL")
	)
	flag.Parse()

	if *umlFile == "" || *outputPath == "" {
		fmt.Println("❌ --uml and --output are required")
		flag.Usage()
		os.Exit(1)
	}

	fmt.Println("🚀 UML-to-CRUD Generator (Go CLI)")

	switch *command {
	case "generate":
		generateCode(*umlFile, *outputPath, *framework, *packageName, *apiURL)
	case "validate":
		validateUML(*umlFile, *apiURL)
	default:
		fmt.Printf("❌ Unknown command: %s\n", *command)
		os.Exit(1)
	}
}

func generateCode(umlFile, outputPath, framework, packageName, apiURL string) {
	fmt.Printf("📁 Generating %s project from %s\n", framework, umlFile)

	// Read UML file
	umlContent, err := os.ReadFile(umlFile)
	if err != nil {
		fmt.Printf("❌ Error reading UML file: %v\n", err)
		os.Exit(1)
	}

	// Prepare request
	request := GenerateRequest{
		UmlContent:     string(umlContent),
		PackageName:    packageName,
		OutputPath:     outputPath,
		GenerationType: "COMPLETE_PROJECT",
		Framework:      convertFramework(framework),
	}

	jsonData, err := json.Marshal(request)
	if err != nil {
		fmt.Printf("❌ Error marshaling JSON: %v\n", err)
		os.Exit(1)
	}

	// Make HTTP request
	resp, err := http.Post(apiURL+"/api/v2/generate/files", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		fmt.Printf("❌ Error making request: %v\n", err)
		os.Exit(1)
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		fmt.Printf("❌ Error reading response: %v\n", err)
		os.Exit(1)
	}

	if resp.StatusCode == 200 {
		var response GenerateResponse
		json.Unmarshal(body, &response)
		
		if response.Success {
			fmt.Printf("✅ %s\n", response.Message)
			fmt.Printf("📊 Generated %d classes\n", response.ClassCount)
			showNextSteps(framework, outputPath)
		} else {
			fmt.Printf("❌ Generation failed: %s\n", response.Message)
		}
	} else {
		fmt.Printf("❌ API Error: %d\n", resp.StatusCode)
		fmt.Println(string(body))
	}
}

func validateUML(umlFile, apiURL string) {
	fmt.Printf("🔍 Validating %s\n", umlFile)

	umlContent, err := os.ReadFile(umlFile)
	if err != nil {
		fmt.Printf("❌ Error reading UML file: %v\n", err)
		os.Exit(1)
	}

	jsonData, _ := json.Marshal(string(umlContent))
	resp, err := http.Post(apiURL+"/api/generate/validate", "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		fmt.Printf("❌ Error making request: %v\n", err)
		os.Exit(1)
	}
	defer resp.Body.Close()

	if resp.StatusCode == 200 {
		fmt.Println("✅ UML validation completed")
	} else {
		fmt.Println("❌ Validation failed")
	}
}

func convertFramework(framework string) string {
	switch framework {
	case "spring-boot":
		return "SPRING_BOOT"
	case "django":
		return "DJANGO"
	case "flask":
		return "FLASK"
	case "dotnet":
		return "DOTNET"
	case "express":
		return "EXPRESS"
	default:
		return "SPRING_BOOT"
	}
}

func showNextSteps(framework, outputPath string) {
	fmt.Println("\n🎯 Next Steps:")
	switch framework {
	case "spring-boot":
		fmt.Printf("  cd %s\n", outputPath)
		fmt.Println("  mvn spring-boot:run")
	case "django":
		fmt.Printf("  cd %s\n", outputPath)
		fmt.Println("  pip install -r requirements.txt")
		fmt.Println("  python manage.py runserver")
	case "dotnet":
		fmt.Printf("  cd %s\n", outputPath)
		fmt.Println("  dotnet run")
	}
}