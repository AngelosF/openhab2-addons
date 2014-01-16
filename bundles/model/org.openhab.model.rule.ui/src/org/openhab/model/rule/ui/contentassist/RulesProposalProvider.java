/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/*
* generated by Xtext
*/
package org.openhab.model.rule.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.xbase.XbasePackage;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.ui.internal.RuleModelUIActivator;
import org.openhab.model.script.scoping.StateAndCommandProvider;

import com.google.common.base.Function;
import com.google.inject.Inject;
/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
@SuppressWarnings("restriction")
public class RulesProposalProvider extends AbstractRulesProposalProvider {
	
	@Inject
	StateAndCommandProvider stateAndCommandProvider;
	
	@Override
	public void completeScript_Expressions(EObject model,
			Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		super.completeScript_Expressions(model, assignment, context, acceptor);
		if (model == null || model instanceof Rule ) {
			Function<IEObjectDescription, ICompletionProposal> proposalFactory = getProposalFactory(getFeatureCallRuleName(), context);
			IScope scope = getScopeProvider().createSimpleFeatureCallScope(model, XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE, context.getResource(), false, -1);
			createLocalVariableAndImplicitProposals(model, context, acceptor);
		}
	}
	
	@Override
	public void complete_ItemName(EObject model, RuleCall ruleCall,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_ItemName(model, ruleCall, context, acceptor);
		ItemRegistry itemRegistry = RuleModelUIActivator.itemRegistryTracker.getService();
		
		for(Item item : itemRegistry.getItems()) {
			if(item.getName().startsWith(context.getPrefix())) {
				acceptor.accept(createCompletionProposal(item.getName(), context));
			}
		}
	}
	
	@Override
	public void complete_ValidState(EObject model, RuleCall ruleCall,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_ValidState(model, ruleCall, context, acceptor);
		for(State state : stateAndCommandProvider.getAllStates()) {
			if(state.toString().startsWith(context.getPrefix())) {
				acceptor.accept(createCompletionProposal(state.toString(), context));
			}
		}
	}

	@Override
	public void complete_ValidCommand(EObject model, RuleCall ruleCall,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		super.complete_ValidState(model, ruleCall, context, acceptor);
		for(Command command : stateAndCommandProvider.getAllCommands()) {
			if(command.toString().startsWith(context.getPrefix())) {
				acceptor.accept(createCompletionProposal(command.toString(), context));
			}
		}
	}
}
