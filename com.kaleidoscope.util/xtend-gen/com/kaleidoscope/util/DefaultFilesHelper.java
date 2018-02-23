package com.kaleidoscope.util;

import com.kaleidoscope.util.PathUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class DefaultFilesHelper {
  public static String generateDefaultEPackageForProject(final String projectName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"ASCII\"?>");
    _builder.newLine();
    _builder.append("<ecore:EPackage xmi:version=\"2.0\" ");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("xmlns:xmi=\"http://www.omg.org/XMI\" ");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("xmlns:ecore=\"http://www.eclipse.org/emf/2002/Ecore\" ");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("name=\"");
    String _lastSegmentOf = PathUtil.lastSegmentOf(projectName);
    _builder.append(_lastSegmentOf, "\t\t\t\t");
    _builder.append("\" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.append("nsURI=\"");
    URI _defaultURIToEcoreFileInPlugin = PathUtil.getDefaultURIToEcoreFileInPlugin(projectName);
    _builder.append(_defaultURIToEcoreFileInPlugin, "\t\t\t\t");
    _builder.append("\" ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.append("nsPrefix=\"");
    _builder.append(projectName, "\t\t\t\t");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    _builder.append("</ecore:EPackage>\t\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String generateDefaultSchema(final String projectName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Add imports here");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#schema ");
    _builder.append(projectName);
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("#source {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#target { ");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("} ");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#correspondence {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#attributeConditions {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String generateDefaultRule(final String schema, final String ruleName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#using ");
    _builder.append(schema);
    _builder.append(".*");
    _builder.newLineIfNotEmpty();
    _builder.append("#using AttrCondDefLibrary.*");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#rule ");
    _builder.append(ruleName);
    _builder.append(" #with ");
    _builder.append(schema);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#source { ");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#target {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#correspondence {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#attributeConditions {");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String generateDefaultAttrCondDefLibrary() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#library AttrCondDefLibrary {");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics:  0:Object == 1:Object");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("eq(0: , 1: ) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BB, BF, FB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BB, BF, FB, FF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:Prefix + 1:Word = 2:Result (where + is string concatenation)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("addPrefix(0:EString, 1:EString, 2:EString) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB, BFF, FBF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:Suffix + 1:Word = 2:Result (where + is string concatenation)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("addSuffix(0:EString, 1:EString, 2:EString) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB, BFF, FFF, FBF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 1:LeftWord + 0:Separator + 2:RightWord = 3:Result (where + is string concatenation)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Note:  0:Separator should be occur only once in 3:Result");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("concat(0:EString, 1:EString, 2:EString, 3:EString) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBBB, BBBF, BBFB, BFFB, BFBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBBB, BBBF, BBFB, BFFB, BFBB, BFFF, BFBF, BBFF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:VariableString is set to 1:DefaultString if it is free (FB).");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//            If it already has a value (BB) then nothing is done and the condition is still satisfied.");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//            The case (_F) does not make sense for #sync as this should be a fixed default string.");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setDefaultString(0:EString, 1:EString) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BB, FB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BB, FB, FF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:VariableNumber is set to 1:DefaultNumber if it is free (FB).");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//            If it already has a value (BB) then nothing is done and the condition is still satisfied.");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//            The case (_F) does not make sense for #sync as this should be a fixed default number.");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setDefaultNumber(0:Number, 1:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BB, FB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BB, FB, FF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics:  new Double(0:String) == 1:Double (where == is equality for doubles)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stringToDouble(0:EString, 1:EDouble) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BB, BF, FB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BB, BF, FB, FF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics:  new Double(0:String) == 1:Int (where == is equality for ints)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stringToInt(0:EString, 1:EInt) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BB, BF, FB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BB, BF, FB, FF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics:  0:Operand * 1:Operand == 2:Result");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("multiply(0:Number, 1:Number, 2:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:Numerator / 1:Denominator == 2:Result (/ is division for doubles)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("divide(0:Number, 1:Number, 2:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:a + 1:b == 2:c (where + is addition for Numbers)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("add(0:Number, 1:Number, 2:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB, FFB, FBF, BFF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:a - 1:b == 2:c");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("sub(0:Number, 1:Number, 2:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB, FFB, BFF, FBF, FFF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 2:c == max(0:a, 1:b)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("max(0:Number, 1:Number, 2:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BBB, BBF, BFB, FBB");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Semantics: 0:a <= 1:b");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("// Note: For FB, BF, and FF, both a and b are set to the same value.");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("smallerOrEqual(0:Number, 1:Number) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#sync: BB, BF, FB");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("#gen: BB, BF, FB, FF");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
}