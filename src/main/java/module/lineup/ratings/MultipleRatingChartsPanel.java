package module.lineup.ratings;

import core.gui.theme.HOColorName;
import core.gui.theme.ThemeManager;
import core.model.HOVerwaltung;
import core.model.UserParameter;
import core.util.Helper;
import core.util.chart.HOLinesChart;
import core.util.chart.LinesChartDataModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JCheckBox;

public final class MultipleRatingChartsPanel extends JPanel {

	private final class SingleChart {

		private HOLinesChart oChartPanel;
		private LinesChartDataModel[] data = new LinesChartDataModel[1];
		private double[] m_Data;
		private NumberFormat m_fmt;
		private final String m_SerieName;
		private final boolean m_isType2;

		public SingleChart(double[] values, NumberFormat format, String serieName) {
			this(values, format, serieName, false);
		}

		public SingleChart(double[] values, NumberFormat format, String serieName, boolean bType2) {
			m_Data = values;
			m_fmt = format;
			m_SerieName = serieName;
			m_isType2 = bType2;
			initComponents();
		}


		private void initComponents() {
			if(m_isType2) {
				data[0] = new LinesChartDataModel(m_Data, m_SerieName, true, ThemeManager.getColor(HOColorName.PALETTE13[1]), m_fmt);
				oChartPanel = new HOLinesChart(false, m_SerieName, null, null, null, null, null, null, null, false);
			}
			else{
				data[0] = new LinesChartDataModel(m_Data, m_SerieName, true, ThemeManager.getColor(HOColorName.PALETTE13[0]), m_fmt);
				oChartPanel = new HOLinesChart(false, m_SerieName, null, null, null, 0d, 20d, null, null, false);
			}
			oChartPanel.setAllValues(data, xAxisDataCaptions, false, showHelpLines.isSelected());
			oChartPanel.setTitle(m_SerieName);
		}

		public void setHelpLines(boolean state) {
			oChartPanel.setHelpLines(state);
		}

		public void setValues(boolean state) {
			oChartPanel.setLabelling(state);
		}

		public JPanel getChartPanel() {
			return oChartPanel.getPanel();
		}
	}

	private HOVerwaltung hov = HOVerwaltung.instance();
	private UserParameter userParameter = UserParameter.instance();
	private JPanel controlsPanel = new JPanel();
	private JPanel chartsPanel = new JPanel(new GridBagLayout());
	private JCheckBox showHelpLines = new JCheckBox(Helper.getTranslation("Hilflinien"), userParameter.MultipleRatingChartsPanel_HelpLines);
	private RatingChartData chartData;
	private ArrayList<Integer> xAxisDataCaptions;
	private SingleChart leftDefense;
	private SingleChart centralDefense;
	private SingleChart rightDefense;
	private SingleChart hatStats;
	private SingleChart midfield;
	private SingleChart loddar;
	private SingleChart leftAttack;
	private SingleChart centralAttack;
	private SingleChart rightAttack;

	public MultipleRatingChartsPanel(RatingChartData data) {
		super(new BorderLayout());
		chartData = data;
		initComponents();
	}

	void prepareCharts() {
		chartsPanel.removeAll();
		chartsPanel.revalidate();
		chartsPanel.repaint();

		DecimalFormat fmt = Helper.DEFAULTDEZIMALFORMAT;

		xAxisDataCaptions = chartData.getCaptions();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill =  GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		leftDefense = new SingleChart(chartData.getLeftDefense(),fmt , Helper.getTranslation("ls.match.ratingsector.leftdefence"));
		chartsPanel.add(leftDefense.getChartPanel(), gbc);

		gbc.gridx = 1;
		centralDefense = new SingleChart(chartData.getCentralDefense(),	fmt, Helper.getTranslation("ls.match.ratingsector.centraldefence"));
		chartsPanel.add(centralDefense.getChartPanel(), gbc);

		gbc.gridx = 2;
		rightDefense = new SingleChart(chartData.getRightDefense(),	Helper.DEFAULTDEZIMALFORMAT, Helper.getTranslation("ls.match.ratingsector.rightdefence"));
		chartsPanel.add(rightDefense.getChartPanel(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		hatStats = new SingleChart(chartData.getHatStats(),	Helper.INTEGERFORMAT, Helper.getTranslation("ls.match.ratingtype.hatstats"), true);
		chartsPanel.add(hatStats.getChartPanel(), gbc);

		gbc.gridx = 1;
		midfield = new SingleChart(chartData.getMidfield(),
									fmt,
									Helper.getTranslation("ls.match.ratingsector.midfield"));
		chartsPanel.add(midfield.getChartPanel(), gbc);

		gbc.gridx = 2;
		loddar = new SingleChart(chartData.getLoddar(), fmt, Helper.getTranslation("ls.match.ratingtype.loddarstats"), true);
		chartsPanel.add(loddar.getChartPanel(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		leftAttack = new SingleChart(chartData.getLeftAttack(),
									fmt,
									Helper.getTranslation("ls.match.ratingsector.leftattack"));
		chartsPanel.add(leftAttack.getChartPanel(), gbc);

		gbc.gridx = 1;
		centralAttack = new SingleChart(chartData.getCentralAttack(),
										fmt,
										Helper.getTranslation("ls.match.ratingsector.centralattack"));
		chartsPanel.add(centralAttack.getChartPanel(), gbc);

		gbc.gridx = 2;
		rightAttack = new SingleChart(chartData.getRightAttack(),
										fmt,
										Helper.getTranslation("ls.match.ratingsector.rightattack"));
		chartsPanel.add(rightAttack.getChartPanel(), gbc);
	}

	private void initComponents() {
		showHelpLines.addItemListener(e -> {
			boolean selected;
			if (e.getStateChange() == ItemEvent.SELECTED) selected = true;
			else if (e.getStateChange() == ItemEvent.DESELECTED) selected = false;
			else return;
			leftDefense.setHelpLines(selected);
			centralDefense.setHelpLines(selected);
			rightDefense.setHelpLines(selected);
			hatStats.setHelpLines(selected);
			midfield.setHelpLines(selected);
			loddar.setHelpLines(selected);
			leftAttack.setHelpLines(selected);
			centralAttack.setHelpLines(selected);
			rightAttack.setHelpLines(selected);
			userParameter.MultipleRatingChartsPanel_HelpLines = selected;
		});

		controlsPanel.add(showHelpLines);
		add(controlsPanel, BorderLayout.SOUTH);

		prepareCharts();
		add(chartsPanel, BorderLayout.CENTER);
	}
}