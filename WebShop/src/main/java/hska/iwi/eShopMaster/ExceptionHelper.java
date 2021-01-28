package hska.iwi.eShopMaster;

import com.opensymphony.xwork2.ActionSupport;

public class ExceptionHelper {

	public static void addExceptionMessages(Throwable ex, ActionSupport action) {
		if (ex == null)
			return;

		action.addActionError(ex.getLocalizedMessage());
		addExceptionMessages(ex.getCause(), action);
	}

}
