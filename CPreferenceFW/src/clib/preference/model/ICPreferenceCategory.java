package clib.preference.model;

import javax.swing.JPanel;


public interface ICPreferenceCategory {

	public String getName();

	public JPanel getPage();

	public void setRepository(CPreferenceRepository repository);

	public void load();

	public void save();
}
