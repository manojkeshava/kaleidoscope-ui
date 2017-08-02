package com.kaleidoscope.ui.delta.visualisation;

import Deltameta.AddEdgeOP;
import Deltameta.AddNodeOP;
import Deltameta.AttributeChangeOP;
import Deltameta.DeleteEdgeOP;
import Deltameta.DeleteNodeOP;
import Deltameta.Edge;
import Deltameta.Operation;
import Deltameta.OperationalDelta;
import com.kaleidoscope.ui.delta.util.DeltaUtil;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class DeltaPlantUMLGenerator {
  private final LinkedList<EObject> nodesToBeDrawn = CollectionLiterals.<EObject>newLinkedList();
  
  public String wrapInTags(final String body) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@startuml");
    _builder.newLine();
    _builder.append("digraph root {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fontname=Monospace");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fontsize=9");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("label=\"\";");
    _builder.newLine();
    _builder.append(body, "");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("@enduml");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String emptyDiagram() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("title Choose an element that can be visualised");
    _builder.newLine();
    return _builder.toString();
  }
  
  public String handleOperation(final Operation delta) {
    if (((delta instanceof AddNodeOP) || (delta instanceof DeleteNodeOP))) {
      return this.handleNodeOperations(delta);
    } else {
      if (((delta instanceof AddEdgeOP) || (delta instanceof DeleteEdgeOP))) {
        return this.handleEdgeOperations(delta);
      } else {
        if ((delta instanceof AttributeChangeOP)) {
          return this.handleAttributeChange(((AttributeChangeOP)delta));
        } else {
          return "";
        }
      }
    }
  }
  
  public String handleOperations(final OperationalDelta delta) {
    EList<Operation> _operations = delta.getOperations();
    final Function1<Operation, String> _function = (Operation o) -> {
      return this.handleOperation(o);
    };
    List<String> _map = ListExtensions.<Operation, String>map(_operations, _function);
    return IterableExtensions.join(_map);
  }
  
  private String handleNodeOperations(final Operation op) {
    String color = null;
    EObject node = null;
    final String fontname = "Monospace";
    final int penwidth = 1;
    final int fontsize = 8;
    String shape = "record";
    String fillcolor = "WHITE";
    final String style = "filled";
    if ((op instanceof AddNodeOP)) {
      color = "GREEN";
      EObject _node = ((AddNodeOP) op).getNode();
      node = _node;
    } else {
      if ((op instanceof DeleteNodeOP)) {
        color = "RED";
        EObject _node_1 = ((DeleteNodeOP) op).getNode();
        node = _node_1;
      } else {
        return "";
      }
    }
    final String nodeID = DeltaUtil.createGoodNameToIdentify(node);
    this.nodesToBeDrawn.remove(node);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(nodeID, "");
    _builder.append("\" [fontsize=");
    _builder.append(fontsize, "");
    _builder.append(", fontname=");
    _builder.append(fontname, "");
    _builder.append(", penwidth=");
    _builder.append(penwidth, "");
    _builder.append(", shape=");
    _builder.append(shape, "");
    _builder.append(", color=");
    _builder.append(color, "");
    _builder.append(", fillcolor=");
    _builder.append(fillcolor, "");
    _builder.append(", label=\"{");
    _builder.append(nodeID, "");
    _builder.append(" | }\",style=");
    _builder.append(style, "");
    _builder.append("];");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  private String handleAttributeChange(final AttributeChangeOP attrChange) {
    final String fontname = "Monospace";
    final int penwidth = 1;
    final String color = "BLACK";
    final int fontsize = 8;
    String shape = "record";
    String fillcolor = "WHITE";
    final String style = "filled";
    final Object newValue = attrChange.getNewValue();
    EObject _node = attrChange.getNode();
    final String nodeID = DeltaUtil.createGoodNameToIdentify(_node);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(nodeID, "");
    _builder.append("\" [fontsize=");
    _builder.append(fontsize, "");
    _builder.append(", fontname=");
    _builder.append(fontname, "");
    _builder.append(", penwidth=");
    _builder.append(penwidth, "");
    _builder.append(", shape=");
    _builder.append(shape, "");
    _builder.append(", color=");
    _builder.append(color, "");
    _builder.append(", fillcolor=");
    _builder.append(fillcolor, "");
    _builder.append(", label=\"{");
    _builder.append(nodeID, "");
    _builder.append(" | value: OLD VALUE ==\\> ");
    _builder.append(newValue, "");
    _builder.append("}\",style=");
    _builder.append(style, "");
    _builder.append("];");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  private String handleEdgeOperations(final Operation op) {
    Edge edge = null;
    String color = null;
    final String fontname = "Monospace";
    final int penwidth = 1;
    String shape = "record";
    final String style = "filled";
    String fillcolor = "WHITE";
    final int fontsize = 8;
    final boolean constraint = true;
    if ((op instanceof AddEdgeOP)) {
      color = "GREEN";
      Edge _edge = ((AddEdgeOP) op).getEdge();
      edge = _edge;
    } else {
      if ((op instanceof DeleteEdgeOP)) {
        color = "RED";
        Edge _edge_1 = ((DeleteEdgeOP) op).getEdge();
        edge = _edge_1;
      } else {
        return "";
      }
    }
    final EObject srcEObject = edge.getSrc();
    final EObject trgEObject = edge.getTrg();
    EReference _type = edge.getType();
    final String label = _type.getName();
    final String srcNodeID = DeltaUtil.createGoodNameToIdentify(srcEObject);
    final String trgNodeID = DeltaUtil.createGoodNameToIdentify(trgEObject);
    this.nodesToBeDrawn.add(srcEObject);
    this.nodesToBeDrawn.add(trgEObject);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    _builder.append(srcNodeID, "");
    _builder.append("\" [fontsize=");
    _builder.append(fontsize, "");
    _builder.append(", fontname=");
    _builder.append(fontname, "");
    _builder.append(", penwidth=");
    _builder.append(penwidth, "");
    _builder.append(", shape=");
    _builder.append(shape, "");
    _builder.append(", color=BLACK, fillcolor=");
    _builder.append(fillcolor, "");
    _builder.append(", label=\"{");
    _builder.append(srcNodeID, "");
    _builder.append(" | }\",style=");
    _builder.append(style, "");
    _builder.append("];");
    _builder.newLineIfNotEmpty();
    _builder.append("\"");
    _builder.append(trgNodeID, "");
    _builder.append("\" [fontsize=");
    _builder.append(fontsize, "");
    _builder.append(", fontname=");
    _builder.append(fontname, "");
    _builder.append(", penwidth=");
    _builder.append(penwidth, "");
    _builder.append(", shape=");
    _builder.append(shape, "");
    _builder.append(", color=");
    _builder.append(color, "");
    _builder.append(", fillcolor=");
    _builder.append(fillcolor, "");
    _builder.append(", label=\"{");
    _builder.append(trgNodeID, "");
    _builder.append(" | }\",style=");
    _builder.append(style, "");
    _builder.append("];");
    _builder.newLineIfNotEmpty();
    _builder.append("\"");
    _builder.append(srcNodeID, "");
    _builder.append("\" -> \"");
    _builder.append(trgNodeID, "");
    _builder.append("\" [fontname=");
    _builder.append(fontname, "");
    _builder.append(", penwidth=");
    _builder.append(penwidth, "");
    _builder.append(", color=");
    _builder.append(color, "");
    _builder.append(", label=\"");
    _builder.append(label, "");
    _builder.append("\", fontsize=");
    _builder.append(fontsize, "");
    _builder.append(", constraint=");
    _builder.append(constraint, "");
    _builder.append("];");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
}