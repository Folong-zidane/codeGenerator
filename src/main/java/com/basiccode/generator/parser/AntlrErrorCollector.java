package com.basiccode.generator.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * ANTLR ErrorListener that collects diagnostics instead of throwing
 */
public class AntlrErrorCollector extends BaseErrorListener {
    
    private final List<Diagnostic> diagnostics = new ArrayList<>();
    
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                           int line, int charPositionInLine, String msg, RecognitionException e) {
        
        String suggestion = generateSuggestion(msg, offendingSymbol);
        diagnostics.add(Diagnostic.error(msg, line, charPositionInLine));
        
        if (suggestion != null) {
            diagnostics.add(Diagnostic.warning("Suggestion: " + suggestion, line, charPositionInLine, suggestion));
        }
    }
    
    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                               boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
        diagnostics.add(Diagnostic.warning(
            "Ambiguous grammar at position " + startIndex + "-" + stopIndex,
            0, startIndex, "Consider simplifying the diagram syntax"
        ));
    }
    
    private String generateSuggestion(String errorMsg, Object offendingSymbol) {
        if (errorMsg.contains("missing")) {
            return "Check for missing punctuation or keywords";
        }
        if (errorMsg.contains("extraneous")) {
            return "Remove unexpected characters or symbols";
        }
        if (errorMsg.contains("no viable alternative")) {
            return "Check diagram syntax - this construct may not be supported";
        }
        return null;
    }
    
    public List<Diagnostic> getDiagnostics() {
        return new ArrayList<>(diagnostics);
    }
    
    public boolean hasErrors() {
        return diagnostics.stream().anyMatch(d -> d.getLevel() == DiagnosticLevel.ERROR);
    }
    
    public void clear() {
        diagnostics.clear();
    }
}