package com.logicaldoc.gui.frontend.client.zoho;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.logicaldoc.gui.common.client.i18n.I18N;
import com.logicaldoc.gui.common.client.log.GuiLog;
import com.logicaldoc.gui.common.client.observer.FolderController;
import com.logicaldoc.gui.common.client.util.LD;
import com.logicaldoc.gui.frontend.client.document.DocumentsPanel;
import com.logicaldoc.gui.frontend.client.folder.FolderNavigator;
import com.logicaldoc.gui.frontend.client.panels.MainPanel;
import com.logicaldoc.gui.frontend.client.search.SearchPanel;
import com.logicaldoc.gui.frontend.client.services.ZohoService;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 * This is the form used to select an element in Zoho
 * 
 * @author Marco Meschieri - LogicalDOC
 * @since 7.5.2
 */
public class ZohoDialog extends Dialog {

	private TreeGrid tree = null;

	private boolean export = false;

	public ZohoDialog(boolean export) {
		super();
		this.export = export;
		setHeaderControls(HeaderControls.HEADER_LABEL, HeaderControls.CLOSE_BUTTON);
		setTitle(I18N.message("zoho"));
		setWidth(350);
		setHeight(300);
		setCanDragResize(true);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		setPadding(3);

		VLayout content = new VLayout();
		content.setTop(10);
		content.setWidth100();
		content.setHeight100();
		content.setMembersMargin(3);

		tree = new ZohoTree(export);
		tree.setWidth100();
		tree.setHeight100();

		VLayout buttons = new VLayout();
		buttons.setWidth100();
		buttons.setHeight(30);

		Button select = new Button(I18N.message("select"));
		select.setAutoFit(true);
		select.setMargin(1);
		select.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (ZohoDialog.this.export)
					onExport();
				else
					onImport();
			}
		});

		buttons.setMembers(select);

		content.setMembers(tree, buttons);
		addItem(content);
	}

	private void onExport() {
		final Record selection = tree.getSelectedRecord();
		if (selection == null)
			return;

		final long[] docIds = MainPanel.get().isOnDocumentsTab() ? DocumentsPanel.get().getDocumentsGrid()
				.getSelectedIds() : SearchPanel.get().getDocumentsGrid().getSelectedIds();

		SC.ask(docIds.length == 0 ? I18N.message("exportdirtozoho", FolderController.get().getCurrentFolder().getName()) : I18N
				.message("exportdocstozoho"), new BooleanCallback() {

			@Override
			public void execute(Boolean choice) {
				if (choice.booleanValue()) {
					String targetId = selection.getAttributeAsString("id");
					long[] folderIds = new long[0];
					if (docIds.length == 0 && FolderController.get().getCurrentFolder() != null)
						folderIds[0] = FolderController.get().getCurrentFolder().getId();

					LD.contactingServer();
					ZohoService.Instance.get().exportDocuments(targetId, folderIds, docIds,
							new AsyncCallback<Boolean>() {
								@Override
								public void onFailure(Throwable caught) {
									LD.clearPrompt();
									GuiLog.serverError(caught);
								}

								@Override
								public void onSuccess(Boolean result) {
									LD.clearPrompt();
									if (result.booleanValue()) {
										SC.say(I18N.message("zohoexportok"));
										ZohoDialog.this.destroy();
									} else
										SC.say(I18N.message("zohoexportko"));
								}
							});
				}
			}
		});
	}

	private void onImport() {
		final Record[] selection = tree.getSelectedRecords();
		if (selection == null)
			return;

		final List<String> docIds = new ArrayList<String>();
		final List<String> folderCompositeIds = new ArrayList<String>();
		for (Record record : selection) {
			if ("folder".equals(record.getAttributeAsString("type")))
				folderCompositeIds.add(record.getAttributeAsString("name") + ":" + record.getAttributeAsString("id"));
			else
				docIds.add(record.getAttributeAsString("id"));
		}
		SC.ask(I18N.message("importfromzoho", FolderController.get().getCurrentFolder().getName()), new BooleanCallback() {

			@Override
			public void execute(Boolean choice) {
				if (choice.booleanValue()) {
					ZohoDialog.this.destroy();
					LD.contactingServer();
					ZohoService.Instance.get().importDocuments(FolderController.get().getCurrentFolder().getId(),
							folderCompositeIds.toArray(new String[0]), docIds.toArray(new String[0]),
							new AsyncCallback<Integer>() {
								@Override
								public void onFailure(Throwable caught) {
									LD.clearPrompt();
									GuiLog.serverError(caught);
								}

								@Override
								public void onSuccess(Integer count) {
									LD.clearPrompt();
									FolderNavigator.get().reload();
									SC.say(I18N.message("importeddocs2", count.toString()));
								}
							});
				}
			}
		});
	}
}