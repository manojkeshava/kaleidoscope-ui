package com.kaleidoscope.ui.delta.visualisation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IEditorPart;

import Delta.Operation;
import Delta.OperationalDelta;
import Delta.StructuralDelta;
import net.sourceforge.plantuml.eclipse.utils.DiagramTextProvider;


public class DeltaDiagramTextProvider implements DiagramTextProvider  {
	private EcoreEditor currentEditor;
	
	@Override
	public String getDiagramText(IEditorPart editorPart, ISelection selected) {
		EObject selectedElement = getSelectedObject(selected);
		
		if (selectedElement != null && isElementValidInput(selectedElement)) {
			DeltaPlantUMLGenerator gen = new DeltaPlantUMLGenerator();
			// Extract input object
			if(selectedElement instanceof Operation)
				return gen.wrapInTags(gen.handleOperation((Operation)selectedElement));
			else if(selectedElement instanceof StructuralDelta)
				return gen.wrapInTags(gen.handleSDelta((StructuralDelta)selectedElement));
			else if(selectedElement instanceof OperationalDelta) {
				OperationalDelta odelta = (OperationalDelta)EcoreUtil.copy(selectedElement);
				com.kaleidoscope.core.delta.javabased.operational.OperationalDelta odeltaJava = com.kaleidoscope.core.delta.javabased.operational.OperationalDelta.fromEMF(odelta);
				StructuralDelta sdelta = odeltaJava.transformToStructuralDelta().toEMF();
				return gen.wrapInTags(gen.handleSDelta(sdelta));
			}
		}

		return "";
	}

	@Override
	public boolean supportsSelection(ISelection selection) {
		return isElementValidInput(getSelectedObject(selection));
	}
	
	   
	protected EObject getInput(EObject selectedElement){
		   return selectedElement;
	}
	
	public boolean isElementValidInput(Object selectedElement) {
		return  selectedElement instanceof Operation ||
			    selectedElement instanceof StructuralDelta ||
			    selectedElement instanceof OperationalDelta;
	}


	private EObject getSelectedObject(ISelection selection) {
		if (selection != null && !selection.isEmpty() && selection instanceof TreeSelection) {
			StructuredSelection structuredSelection = (StructuredSelection) selection;
			if (structuredSelection.getFirstElement() instanceof EObject) {
				return (EObject) structuredSelection.getFirstElement();
			}
		}

		return null;
	}
	 
	@Override
	public boolean supportsEditor(IEditorPart editorPart) {
		EObject selectedElement = getSelectedObject(editorPart.getSite().getSelectionProvider().getSelection());

		if (selectedElement == null || !isElementValidInput(selectedElement))
			return false;

		if (editorPart.equals(currentEditor))
			return true;

		if (editorPart instanceof EcoreEditor) {
			currentEditor = (EcoreEditor) editorPart;
			return true;
		}

		return false;
	}
}
