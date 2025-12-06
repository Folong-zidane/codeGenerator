package com.basiccode.generator.parser;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.*;

/**
 * 🚀 Parser pour modèles Django - Support du DjangoModelGenerator avancé
 */
public class DjangoModelParser {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DjangoModels {
        private List<DjangoModel> modelsList = new ArrayList<>();
    }
    
    @Data
    @NoArgsConstructor
    public static class DjangoModel {
        private String name;
        private String docstring;
        private List<DjangoField> fields = new ArrayList<>();
        private List<DjangoMethod> methods = new ArrayList<>();
        private List<DjangoMethod> validators = new ArrayList<>();
        private List<String> uniqueTogether = new ArrayList<>();
        private boolean isAbstract = false;
    }
    
    @Data
    @NoArgsConstructor
    public static class DjangoField {
        private String name;
        private String fieldType;
        private String target; // Pour ForeignKey
        private List<String> validators = new ArrayList<>();
        private Map<String, String> options = new HashMap<>();
        
        public void addValidator(String validator) {
            this.validators.add(validator);
        }
        
        public String toPythonCode() {
            StringBuilder code = new StringBuilder();
            code.append(name).append(" = models.").append(fieldType).append("(");
            
            // Options
            List<String> optionsList = new ArrayList<>();
            for (Map.Entry<String, String> entry : options.entrySet()) {
                optionsList.add(entry.getKey() + "=" + entry.getValue());
            }
            
            // Validators
            if (!validators.isEmpty()) {
                optionsList.add("validators=[" + String.join(", ", validators) + "]");
            }
            
            code.append(String.join(", ", optionsList));
            code.append(")");
            
            return code.toString();
        }
    }
    
    @Data
    @NoArgsConstructor
    public static class DjangoMethod {
        private String name;
        private String signature;
        private String docstring;
        private List<String> parameters = new ArrayList<>();
        
        public String toPythonSignature() {
            if (signature != null) {
                return signature;
            }
            
            StringBuilder sig = new StringBuilder();
            sig.append("def ").append(name).append("(self");
            if (!parameters.isEmpty()) {
                sig.append(", ").append(String.join(", ", parameters));
            }
            sig.append("):");
            
            return sig.toString();
        }
    }
}