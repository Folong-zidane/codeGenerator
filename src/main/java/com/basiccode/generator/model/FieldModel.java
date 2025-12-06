package com.basiccode.generator.model;

/**
 * Modèle représentant un champ avec métadonnées étendues
 */
public class FieldModel extends Field {
    private boolean nullable = true;
    private boolean unique = false;
    private Visibility visibility = Visibility.PRIVATE;
    private String defaultValue;
    private int maxLength = -1;
    private int minLength = -1;
    
    public FieldModel() {
        super();
    }
    
    public FieldModel(String name, String type) {
        super(name, type);
    }
    
    public boolean isNullable() { return nullable; }
    public void setNullable(boolean nullable) { this.nullable = nullable; }
    
    public boolean isUnique() { return unique; }
    public void setUnique(boolean unique) { this.unique = unique; }
    
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
    
    public String getDefaultValue() { return defaultValue; }
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
    
    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
    
    public int getMinLength() { return minLength; }
    public void setMinLength(int minLength) { this.minLength = minLength; }
}