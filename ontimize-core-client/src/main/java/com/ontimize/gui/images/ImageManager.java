package com.ontimize.gui.images;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageManager {

	// public static boolean DEBUG = true;
	private static final Logger logger = LoggerFactory.getLogger(ImageManager.class);

	public static boolean cache = true;

	public static String BUNDLE = "bundle.png";

	public static String BLACK_HAND = "black_hand.png";

	public static final String ONTIMIZE_SPLASH = "ontimize_splash.gif";

	public static final String TABLE_HEADER_33 = "tableheader.jpg";

	public static final String SEPARATOR_ICON_16 = "verticalline16.png";

	public static final String SEPARATOR_ICON_24 = "verticalline24.png";
	
	public static String LOCK = "lock.png";

	public static String LEFT = "left.png";

	public static String RIGHT = "right.png";

	public static String AND = "and.gif";

	public static String OR = "or.png";

	public static String MODIF = "modif.gif";

	public static String AND_NOT = "andnot.gif";

	public static String OR_NOT = "ornot.png";

	public static String GEAR = "gear.gif";

	public static String OPEN_FILE = "openfile.png";

	public static String SAVE_FILE = "savefile.png";

	public static String CANCEL = "cancel.png";

	public static String OK = "ok.png";

	public static String OPTIONS = "options.png";

	public static String LINK_DELETE = "link_delete.png";

	public static String HELP = "help.png";

	public static String PREVIEW = "preview.png";

	public static String SAVE_TABLE_FILTER = "savetablefilter.png";

	public static String OPEN = "open.gif";

	public static String CONF_VISIBLE_COLS = "confvisiblecols.png";

	public static String RECYCLER = "recycler.png";

	public static String NEW_GIF = "new.gif";

	public static String DELETE_GIF = "delete.gif";

	public static String DOCUMENT_DELETE = "document_delete.png";

	public static String OPEN_QUERY = "openquery.gif";

	public static String FUNNEL_EDIT = "funnel_edit.png";

	public static String DELETE = "delete.png";

	public static String CHECK_SELECTED = "checkselected.png";

	public static String CHECK_UNSELECTED = "checkunselected.gif";

	public static String INFO_16 = "info_16.png";

	public static String WARNING = "warning.png";

	public static String FUNNEL_NEW = "funnel_new.png";

	public static String PAGE = "page.png";

	public static String PREV13 = "prev13.gif";

	public static String NEXT13 = "next13.gif";

	public static String MULTIREFQUERY = "multirefquery.png";

	public static String SEARCHING = "searching.png";

	public static String SEARCH = "search.png";

	public static String SEARCH_24 = "search24.png";

	public static String CONFIRM_QUERY = "confirmquery.gif";

	public static String CONFIRM_QUERY_24 = "confirmquery24.gif";

	public static String INSERT = "insert.png";

	public static String INSERT_24 = "insert24.png";

	public static String CONFIRM_INSERT = "confirminsert.gif";

	public static String CONFIRM_INSERT_24 = "confirminsert24.gif";

	public static String UPDATE = "update.png";

	public static String UPDATE_24 = "update24.png";

	public static String DELETE_DOCUMENT = "delete_document.png";

	public static String DELETE_DOCUMENT_24 = "delete_document24.png";

	public static String ADVANCE_SEARCH = "advancesearch.png";

	public static String PLAY = "play.png";

	public static String PAUSE = "pause.png";

	public static String STOP = "stop.png";

	public static String REWIND = "rewind.png";

	public static String FORWARD = "forward.png";

	public static String EXPLORE = "explore.png";

	public static String SOUND = "sound.png";

	public static String EMPTY_SOUND = "emptysound.png";

	public static String CHOOSE_COLOR = "choosecolor.png";

	public static String CALC = "calc.png";

	public static String EURO = "euro.png";

	public static String PESETA = "peseta.gif";

	public static String HELPBOOK = "helpbook.png";

	public static String CALENDAR = "calendar.png";

	public static String ADD_DICTIONARY = "adddictionary.png";

	public static String REMOVE_FROM_DICTIONARY = "removefromdictionary.png";

	public static String TIPS = "tips.png";

	public static String CHANGE_DICTIONARY = "changedictionary.png";

	public static String POPUP_ARROW = "popuparrow.png";

	public static String BOLD_FONT = "boldfont.png";

	public static String ITALIC_FONT = "italicfont.png";

	public static String UNDERLINE_FONT = "underlinefont.png";

	public static String LEFT_ALIGN = "leftalign.png";

	public static String CENTER_ALIGN = "centeralign.png";

	public static String RIGHT_ALIGN = "rightalign.png";

	public static String JUSTIFY_ALIGN = "justifyalign.png";

	public static String LIST_ORDERED = "listordered.png";

	public static String LIST_UNORDERED = "listunordered.png";

	public static String HTML_TABLE = "html_table.png";

	public static String COPY = "copy.png";

	public static String SCANNER = "scanner.png";

	public static String SCANNER_PREVIEW = "scannerpreview.png";

	public static String PASTE = "paste.png";

	public static String MAGNIFYING_GLASS = "magnifyingglass.png";

	public static String REFRESH = "refresh.png";

	public static String INTERNET = "internet.png";

	public static String UNDO = "undo.png";

	public static String REDO = "redo.png";

	public static String DOCUMENT_DOWN = "document_down.gif";

	public static String DOCUMENT_UP = "document_up.gif";

	public static String UP = "up.png";

	public static String DOWN = "down.png";

	public static String PRINT = "print.png";

	public static String FOLDER_SHARED = "folder_shared.png";

	public static String DATA_SHARED = "data_shared.png";

	public static String DATA_SHARED_DELETE = "data_shared_delete.png";

	public static String DATA_SHARE_ACTION = "data_share_action.png";

	public static String COLUMNS_ORDER = "columnsorder.png";

	public static String SELECT_PRINT_COLUMNS = "selectprintcolumns.png";

	public static String PREVIOUS_2_VERTICAL = "previous2vertical.png";

	public static String NEXT_2_VERTICAL = "next2vertical.png";

	public static String END_2_VERTICAL = "end2vertical.png";

	public static String START_2_VERTICAL = "start2vertical.png";

	public static String SAVE_DISC = "savedisc.png";

	public static String SELECTION_UP = "selection_up.png";

	public static String SELECTION_DOWN = "selection_down.png";

	public static String SELECTION_REPLACE = "selection_replace.png";

	public static String CHART = "chart.png";

	public static String LINE_CHART = "linechart.png";

	public static String PIE_CHART = "pie.png";

	public static String PIE = "pie.png";

	public static String BAR = "bar.png";

	public static String STACK = "stack.png";

	public static String DETAIL = "detail.png";

	public static String RESET_ORDER = "resetorder.gif";

	public static String FUNNEL_ADD = "funnel_add.gif";

	public static String FUNNEL_DELETE = "funnel_delete.gif";

	public static String GROUP = "group.gif";

	public static String SUM = "sum2.gif";

	public static String DELETE_GROUP = "deletegroup.gif";

	public static String SHOW_HIDE_CONTROLS = "showhidecontrols.gif";

	public static String OPEN_NEW_WINDOW = "opennewwindow.png";

	public static String EXCEL = "excel.png";

	public static String CONF_VISIBLE_COLS_RED = "confvisiblecolsred.png";

	public static String DEFAULT_CHARTS = "defaultcharts.png";

	public static String EMPTY_16 = "empty16.gif";

	public static String PIVOT = "pivot.png";

	public static String FUNNEL_DELETE_ALL = "funnel_deleteall.gif";

	public static String UPWARD = "upward.png";

	public static String DOWN_GREEN = "down_green.gif";

	public static String NEW = "new.png";

	public static String HELP_ON_ITEM_CURSOR = "helponitemcursor.gif";

	public static String HELP_ON_FIELD_CURSOR = "helponfieldcursor.gif";

	public static String CURSOR_LINK_DISABLE = "cursorlinkdisable.gif";

	public static String CURSOR_DETAIL = "cursordetail.gif";

	public static String ZOOM_CURSOR = "zoomcursor.png";

	public static String ICON_IMATIA = "iconimatia.gif";

	public static String TABLE_REFRESH = "tablerefresh.png";

	public static String ATTACH_FILE = "attachfile.png";

	public static String DELETE_ATTACHMENT = "deleteattachment.png";

	public static String EXT_OP_THREADS_MONITOR = "extopthreadsmonitor.png";

	public static String DOWNLOADING = "downloading.png";

	public static String ERROR = "error.png";

	public static String LEFT_ARROW = "leftarrow.png";

	public static String RIGHT_ARROW = "rightarrow.png";

	public static String ALL_LEFT_ARROW = "allleftarrow.png";

	public static String ALL_RIGHT_ARROW = "allrightarrow.png";

	public static String USER_CONNECT = "userconnect.png";

	public static String USER_CONNECT_2 = "userconnect2.png";

	public static String USER_DISCONNECT = "userdisconnect.png";

	public static String ADD_USER = "adduser.png";

	public static String DELETE_USER = "deleteuser.png";

	public static String NEXT_2 = "next2.png";

	public static String PREVIOUS_2 = "previous2.png";

	public static String END_2 = "end2.png";

	public static String START_2 = "start2.png";

	public static String EXIT = "exit.png";

	public static String CLOSE_SESSION = "closesession.png";

	public static String SEND_TO_TRAY = "sendtotray.png";

	public static String TREE = "tree.png";

	public static String ARRANGE = "arrange.gif";

	public static String REFRESH_2 = "refresh2.png";

	public static String TABLE_VIEW = "tableview.png";

	public static String CHECK = "mask/check.png";

	public static String DELETE_FIELDS = "deletefields.png";

	public static String HELP_2 = "help2.png";

	public static String ADD = "add.png";

	public static String LOCK_APPLICATION_HEADER = "lockapplicationheader.jpg";

	public static String ONTIMIZE = "ontimize.gif";

	public static String ONTIMIZE_LOGO = "logoontimize.jpg";

	public static String LOCK_APPLICATION = "lockedapplication.png";

	public static String VIEW_DETAILS = "viewdetails.png";

	public static String HIDE_DETAILS = "hidedetails.png";

	public static String PREV = "prev.gif";

	public static String NEXT = "next.gif";

	public static String PREV_HELP_UI = "prev_help_ui.png";

	public static String NEXT_HELP_UI = "next_help_ui.png";

	public static String PRINT_HELP_UI = "print_help_ui.png";

	public static String PAGE_SETUP_HELP_UI = "pagesetup_help_ui.png";

	public static String LOADING_HELP = "loadinghelp.png";

	public static String MULTIPLE_USERS = "multipleusers.jpg";

	public static String CLOCK_RESET_36 = "clock_reset36.png";

	public static String PLAY_MEDIA = "media/play.png";

	public static String PAUSE_MEDIA = "media/pause.png";

	public static String STOP_MEDIA = "media/stop.png";

	public static String REWIND_MEDIA = "media/rewind.png";

	public static String FORWARD_MEDIA = "media/forward.png";

	public static String OPEN_MEDIA = "media/open.png";

	public static String LICENSE_WARNING_48 = "com/ontimize/ols/resource/images/licenseWarning-48x48.gif";

	public static String EARTH_LOCK = "com/ontimize/ols/shared/resource/images/earth_lock.png";

	public static String BUNDLE_ICON = "com/ontimize/ols/shared/resource/images/earth_lock.png";

	public static String LICENSE_INFORMATION = "com/ontimize/ols/resource/images/license_information.png";

	public static String ONTIMIZE_48 = "ontimize48.png";

	public static String SAVE = "report/save.png";

	public static String REPORT_DELETE = "report/delete.png";

	public static String CHECK_DESELECTED = "checkdeselected.png";

	public static String REPORT_CANCEL = "report/cancel.png";

	public static String WIZARD = "report/wizard.png";

	public static String SQL = "report/sql.gif";

	public static String REPORT_OK = "report/ok.png";

	public static String CURSOR = "report/cursor.gif";

	public static String LINE = "report/line.png";

	public static String RECTANGLE = "report/rectangle.gif";

	public static String LABEL = "report/label.png";

	public static String STRING_FIELD = "report/stringfield.png";

	public static String IMAGE_FIELD = "report/imagefield.gif";

	public static String IMAGE = "report/image.gif";

	public static String REPORT_NEW = "report/new.png";

	public static String REPORT_OPEN = "report/open.png";

	public static String REPORT_GROUP = "report/group.png";

	public static String PROPERTIES = "report/properties.gif";

	public static String WATERMARK = "report/watermark.gif";

	public static String FUNCTION = "report/function.png";

	public static String REPORT_UP = "report/up.png";

	public static String REPORT_DOWN = "report/down.png";

	public static String NO_IMAGE_2 = "report/noimage2.png";

	public static String EDIT = "edit.png";

	public static String ZOOM_IN = "zoomin.png";

	public static String ZOOM_OUT = "zoomout.png";

	public static String PDF_24 = "pdf24.png";

	public static String ARROW_UP_BLUE = "report/arrow_up_blue.png";

	public static String ARROW_DOWN_BLUE = "report/arrow_down_blue.png";

	public static String STORE = "report/store.png";

	public static String SORT = "report/sort.png";

	public static String FIT_SIZE = "report/fitsize.png";

	public static String INCIDENCE_BUTTON = "report/mail_bug.png";

	public static String IMAGE_NEW_EMAIL = "mail_new.png";

	public static String NOTICE = "notice.png";

	public static String EMPTY_IMAGE = "emptyimage.gif";

	public static String USERS = "users.png";

	public static String USERS_EDIT = "users_edit.png";

	public static String KEYS = "com/ontimize/gui/images/keys.png";

	// Toolbar
	public static String IMAGE_24 = "toolbar/image.png";
	
	public static String KEYS_24 = "toolbar/keys.png";

	public static String USERS_24 = "toolbar/users.png";

	public static String EXIT_24 = "toolbar/exit.png";

	public static String LOCK_24 = "toolbar/lock.png";

	public static String SYSTRAY_24 = "toolbar/systray.png";

	public static String USER_CONNECT_2_24 = "toolbar/userconnect2.png";

	public static String FONT_24 = "toolbar/font.png";

	public static String WINDOW_COLORS_24 = "toolbar/window_colors.png";

	public static String ARROW_UP = "arrow_up.png";

	public static String ARROW_DOWN = "arrow_down.png";

	public static String ARROW_LEFT = "arrow_left.png";

	public static String ARROW_RIGHT = "arrow_right.png";

	public static String ARROW_LEFT_24 = "toolbar/arrow_left.png";

	public static String ARROW_RIGHT_24 = "toolbar/arrow_right.png";

	public static String HOME_24 = "toolbar/home.png";

	public static String BACK_LOGIN = "back_login_ontimize.gif";

	public static String DOC = "com/ontimize/gui/resources/winprogramicons/doc.gif";

	public static String ODT = "com/ontimize/gui/resources/winprogramicons/odt.gif";

	public static String DOCX = "com/ontimize/gui/resources/winprogramicons/docx.png";

	public static String UNKNOWN_EXTENSION = "com/ontimize/gui/resources/winprogramicons/unknown.gif";

	public static String URI_KEY = "key1.gif";

	public static String CALC_ADD = "calc/add.png";

	public static String CALC_SUBSTRACT = "calc/substract.png";

	public static String CALC_MULTIPLY = "calc/multiply.png";

	public static String CALC_DIVIDE = "calc/divide.png";

	public static String CALC_OPEN_PARENTHESIS = "calc/open_parenthesis.png";

	public static String CALC_CLOSE_PARENTHESIS = "calc/close_parenthesis.png";

	public static String CALCULATE = "calc/calculate.png";

	public static String PAPER_SEE_ALL = "report/paper_all.png";

	public static String PAPER_FIRST_PLANE = "report/paper_first_plane.png";

	public static String PAPER_WIDTH = "report/paper_width.png";

	public static String CERTIFICATE_ICON = "id_card.png";

	public static String TABLE_WORKING = "working_64.gif";

	public static String TABLE_LOADING = "loading.gif";

	// Table Buttons.
	public static String TABLE_GROUP = "table_group.png";

	public static String TABLE_COPY = ImageManager.COPY;

	public static String TABLE_EXCEL = ImageManager.EXCEL;

	public static String TABLE_HTML = ImageManager.INTERNET;

	public static String TABLE_INSERT = ImageManager.INSERT;

	public static String TABLE_PRINT = ImageManager.PRINT;

	public static String TABLE_REMOVE = ImageManager.DELETE; // RECYCLER;

	public static String TABLE_CHART = ImageManager.CHART;

	public static String TABLE_DEFAULT_CHARTS = ImageManager.DEFAULT_CHARTS;

	public static String TABLE_SUMROWSETUP = ImageManager.CALC;

	public static String TABLE_SAVE_TABLE_FILTER = ImageManager.SAVE_TABLE_FILTER;

	public static String TABLE_CALCULATEDCOLS = ImageManager.CALCULATE;

	public static String TABLE_PIVOT = ImageManager.PIVOT;

	public static String TABLE_REPORTS = ImageManager.PAGE;

	public static String TABLE_CONF_VISIBLE_COLS = ImageManager.CONF_VISIBLE_COLS;

	public static String TABLE_CONF_VISIBLE_COLS_RED = ImageManager.CONF_VISIBLE_COLS_RED;

	// VisualCalendar Component...
	public static String CALENDAR_PREV = ImageManager.PREV13;

	public static String CALENDAR_NEXT = ImageManager.NEXT13;

	public static boolean databaseStorageAllowed = false;

	private static IImageManager engine;

	public static IImageManager getEngine() {
		if (ImageManager.engine == null) {
			ImageManager.engine = new DefaultImageManager();
		}
		return ImageManager.engine;
	}

	public static void setEngine(IImageManager engine) {
		ImageManager.engine = engine;
	}

	/**
	 * Loads the image corresponding to the image path. Have been rewritten the next paths, in order to use the new system of bundle paths:
	 *
	 * - com/ontimize/gui/images/
	 *
	 * All images that include the above routes have been rewritten, eliminating the above paths, and leaving the rest of the path intact.
	 *
	 * @param icon
	 *            Relative URI to the resource. If the path contains the above paths or any path defined by the user in baseImages, must be deleted from URI string. Example,
	 *            "ok.png" return the image with complete path "com/ontimize/gui/images/ok.png" If the image is in various paths, will be return the first occurrence
	 * @return an ImageIcon corresponding to the resource, or null if the resource is missing
	 */
	public static ImageIcon getIcon(String icon) {
		return ImageManager.getEngine().getIcon(icon);
	}

	public static void resetImageCache() {
		ImageManager.getEngine().resetImageCache();
	}

	public static URL getIconURL(String icon) {
		return ImageManager.getEngine().getIconURL(icon);
	}

	public static void addBaseImagePath(String path) {
		ImageManager.getEngine().addBaseImagePath(path);
	}

	public static List<String> getBaseImagePaths() {
		return ImageManager.getEngine().getBaseImagePaths();
	}

	public static void removeBaseImagePath(String path) {
		ImageManager.getEngine().removeBaseImagePath(path);
	}

	public static ImageIcon transparent(ImageIcon icon, float trans) {
		return ImageManager.getEngine().transparent(icon, trans);
	}

	public static ImageIcon brighter(ImageIcon icon) {
		return ImageManager.getEngine().brighter(icon);
	}

	public static ImageIcon darker(ImageIcon icon) {
		return ImageManager.getEngine().darker(icon);
	}

	public static BufferedImage getBlurImage(BufferedImage image, int radius) {
		return ImageManager.getEngine().getBlurImage(image, radius);
	}

}
