package com.menyi.web.util;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;
import javax.servlet.ServletContext;
import java.util.Locale;

public class ErrorMessage implements ErrorCanst {
    private static MessageResources resource;



    public static String toString(int errorCanst, String loc) {
        if (resource == null) {
            Object o = BaseEnv.servletContext.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
            if (o instanceof MessageResources) {
                resource = (MessageResources) o;
            }
        }
        Locale locale = new Locale(loc);

        String retVal = "";
        switch (errorCanst) {
        case ErrorCanst.DEFAULT_SUCCESS:
            retVal=resource.getMessage(locale, "common.msg.DEFAULT_SUCCESS");
            break;
        case ErrorCanst.DEFAULT_FAILURE:
            retVal=resource.getMessage(locale, "common.msg.DEFAULT_FAILURE");
            break;
        case ErrorCanst.SQLEXCEPTION_ERROR:
            retVal = resource.getMessage(locale, "common.msg.SQLEXCEPTION_ERROR");
            break;
        case ErrorCanst.ER_NO_DATA:
            retVal = resource.getMessage(locale, "common.msg.ER_NO_DATA");
            break;
        case ErrorCanst.COMMIT_ERROR:
            retVal = resource.getMessage(locale, "common.msg.COMMIT_ERROR");
            break;
        case ErrorCanst.ROLLBACK_ERROR:
            retVal = resource.getMessage(locale, "common.msg.ROLLBACK_ERROR");
            break;
        case ErrorCanst.SESSION_CLOSE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.SESSION_CLOSE_ERROR");
            break;
        case ErrorCanst.BEAN_INIT_ERROR:
            retVal = resource.getMessage(locale, "common.msg.BEAN_INIT_ERROR");
            break;
        case ErrorCanst.ILLEGAL_ARGUMENTS:
            retVal = resource.getMessage(locale, "common.msg.ILLEGAL_ARGUMENTS");
            break;
        case ErrorCanst.MULTI_VALUE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.MULTI_VALUE_ERROR");
            break;
        case ErrorCanst.DATA_ALREADY_USED:
            retVal = resource.getMessage(locale, "common.msg.DATA_ALREADY_USED");
            break;
        case ErrorCanst.NUMBER_COMPARE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.NUMBER_COMPARE_ERROR");
            break;
        case ErrorCanst.TIMER_COMPARE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.TIMER_COMPARE_ERROR");
            break;
        case ErrorCanst.RET_NAME_PSW_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_NAME_PSW_ERROR");
            break;
        case ErrorCanst.RET_NO_RIGHT_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_NO_RIGHT_ERROR");
            break;
        case ErrorCanst.RET_PASSWORD_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_PASSWORD_ERROR");
            break;
        case ErrorCanst.RET_ILLEGAL_ARGUMENT_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_ILLEGAL_ARGUMENT_ERROR");
            break;
        case ErrorCanst.RET_DEFAULT_TYPE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_DEFAULT_TYPE_ERROR");
            break;
        case ErrorCanst.RET_TABLENAME_EXIST_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_TABLENAME_EXIST_ERROR");
            break;
        case ErrorCanst.RET_PERANT_TABLE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_PERANT_TABLE_ERROR");
            break;
        case ErrorCanst.RET_ASSOCIATE_NOT_EXIST_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_ASSOCIATE_NOT_EXIST_ERROR");
            break;
        case ErrorCanst.RET_ASSOCIATE_FIELD_TYPE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_ASSOCIATE_FIELD_TYPE_ERROR");
            break;
        case ErrorCanst.RET_EXIST_CHILD_TABLE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_EXIST_CHILD_TABLE_ERROR");
            break;
        case ErrorCanst.RET_TABLE_NOT_EXIST_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_TABLE_NOT_EXIST_ERROR");
            break;
        case ErrorCanst.RET_FORBID_UPDATE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_FORBID_UPDATE_ERROR");
            break;
        case ErrorCanst.RET_FIELD_EXIST_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_FIELD_EXIST_ERROR");
            break;
        case ErrorCanst.RET_FIELD_VALIDATOR_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_FIELD_VALIDATOR_ERROR");
            break;
        case ErrorCanst.RET_ID_NO_VALUE_ERROR:
            retVal = resource.getMessage(locale, "common.msg.RET_ID_NO_VALUE_ERROR");
            break;
        case ErrorCanst.RET_DEFINE_SQL_ERROR:
            retVal = resource.getMessage(locale,"common.msg.RET_DEFINE_SQL_ERROR");
            break;
        case ErrorCanst.RET_DEFINE_SQL_NAME:
            retVal = resource.getMessage(locale,"common.msg.RET_DEFINE_SQL_NAME");
            break;
        case ErrorCanst.RET_DEFINE_SENTENCE_ERROR:
            retVal = resource.getMessage(locale,"common.msg.RET_DEFINE_SENTENCE_ERROR");
            break;
        case ErrorCanst.RET_BEGINACC_END:
            retVal = resource.getMessage(locale,"common.msg.RET_BEGINACC_END");
            break;
        case ErrorCanst.RET_SETTLEACC_END:
            retVal = resource.getMessage(locale,"common.msg.RET_SETTLEACC_END");
            break;
        case ErrorCanst.RET_FUNCTION_LIMIT_ERROR:
            retVal = resource.getMessage(locale,"common.msg.RET_FUNCTION_LIMIT_ERROR");
            break;
        case ErrorCanst.RET_EVALUATE_BIS_LIMIT_ERROR:
            retVal = resource.getMessage(locale,"common.msg.RET_EVALUATE_BIS_LIMIT_ERROR");
            break;
        case ErrorCanst.RET_DEFINE_VALUEOFDB_FORMAT_ERROR:
            retVal = resource.getMessage(locale,"common.msg.RET_DEFINE_VALUEOFDB_FORMAT_ERROR");
            break;
        case ErrorCanst.RET_NOTGRANT_UPDATEACC:
            retVal = resource.getMessage(locale,"common.msg.RET_NOTGRANT_UPDATEACC");
            break;
        case ErrorCanst.RET_NOTREC_RECORDEXCHANGE:
            retVal = resource.getMessage(locale,"common.msg.RET_NOTREC_RECORDEXCHANGE");
            break;
        case ErrorCanst.RET_NOTREC_ADJUSTEXCHANGE:
            retVal = resource.getMessage(locale,"common.msg.RET_NOTREC_ADJUSTEXCHANGE");
            break;
        case ErrorCanst.RET_EXISTS_ADJUSTEXCHANGE:
            retVal = resource.getMessage(locale,"common.msg.RET_EXISTS_ADJUSTEXCHANGE");
            break;
        case ErrorCanst.RET_ACCNOTEQUAL:
            retVal = resource.getMessage(locale,"common.msg.RET_ACCNOTEQUAL");
            break;
        case ErrorCanst.RET_NO_DEFINE_SQL:
        	retVal = resource.getMessage(locale,"common.msg.RET_NO_DEFINE_SQL") ;
        	break ;
        case ErrorCanst.RET_NOTSETROWMARKER:
        	retVal = resource.getMessage(locale,"common.msg.RET_NOTSETROWMARKER") ;
        	break ;
        case ErrorCanst.RET_EXISTSRELATION_ERROR:
        	retVal = resource.getMessage(locale,"common.msg.RET_EXISTSRELATION_ERROR") ;
        	break ;
        case ErrorCanst.RET_LIST_NOCOLUMN:
        	retVal = resource.getMessage(locale,"common.msg.RET_LIST_NOCOLUMN") ;
        	break ;
        case ErrorCanst.RET_BILL_NOTAPPROVE:
        	retVal = resource.getMessage(locale,"common.msg.RET_BILL_NOTAPPROVE") ;
        	break ;
        case ErrorCanst.RET_NOTSETTLE_LASTNOTYEAR:
        	retVal = resource.getMessage(locale,"common.msg.RET_NOTSETTLE_LASTNOTYEAR") ;
        	break ;
        case ErrorCanst.RET_NUMERICAL_OVERFLOW:
        	retVal = resource.getMessage(locale,"common.msg.RET_NUMERICAL_OVERFLOW") ;
        	break ;
        case ErrorCanst.RET_INPRICE_IS_ZERO:
        	retVal = resource.getMessage(locale,"common.msg.RET_INPRICE_IS_ZERO") ;
        	break ;
        case ErrorCanst.RET_FIELD_CANNOTSPEC_ERROR:
        	retVal = resource.getMessage(locale,"common.named.standar") ;
        	break ;
        case ErrorCanst.RET_HAS_VALUE_FORBID_UPDATE:
        	retVal = resource.getMessage(locale,"common.table.not.update") ;
        	break ;
        default:
            retVal = "Error Canst id= " + errorCanst;
            break;
        }

        return retVal;
    }
}
