package com.ruishenh.common.assit.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.ruishenh.common.page.PaginatedArrayList;
import com.ruishenh.common.page.PaginatedQueryCondition;

/**
 * 
 * Mongodb operate database dao tool DESCRIPTION-- "Only so at this operate any
 * business because this biz method only apply to mongodb db"
 * 
 * @param <T>
 */
public class MongoDbDaoTools<T> {

	public static Query takeQueryByCondtion(Map<String, Object> map) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		boolean hasParm = false;
		Map<String, List<Object>> repeatMaps = new HashMap<String, List<Object>>();
		for (String s : map.keySet()) {
			if (checkIsRepeat(s)) {
				mergeList(repeatMaps,
						s.substring(0, s.indexOf(IUtilCondtion.ENUM_REPEAT)),
						map.get(s));
				continue;
			}
			if (null != map.get(s)) {
				if (map.get(s) instanceof UtilCondtion) {
					if (s == null || s.length() == 0) {
						continue;
					}
					UtilCondtion uc = (UtilCondtion) map.get(s);
					// 比较方向eq=0,lt=1,gt=2,gte=3,lte=4
					if (null != uc.getDirCompare()) {
						hasParm = true;
						switch (uc.getDirCompare()) {
						case 0:
							criteria.and(s).is(uc.getCompareValue());
							break;
						case 1:
							criteria.and(s).lt(uc.getCompareValue());
							break;
						case 2:
							criteria.and(s).gt(uc.getCompareValue());
							break;
						case 3:
							criteria.and(s).gte(uc.getCompareValue());
							break;
						case 4:
							criteria.and(s).lte(uc.getCompareValue());
							break;
						default:
							criteria.and(s).is(uc.getCompareValue());
							break;
						}
					}
					if (null != uc.getRegexValue()
							&& uc.getRegexValue().length() > 0) {
						if (null == uc.getDirRegex()) {
							criteria.and(s).is(uc.getRegexValue());
						} else {
							String tmpRegexStr = ".*"
									+ specialEscape(uc.getRegexValue()) + ".*";
							switch (uc.getDirRegex()) {
							case 0:
								tmpRegexStr = ".*"
										+ specialEscape(uc.getRegexValue());
								break;
							case 1:
								tmpRegexStr = ".*"
										+ specialEscape(uc.getRegexValue())
										+ ".*";
								break;
							case 2:
								tmpRegexStr = specialEscape(uc.getRegexValue())
										+ ".*";
								break;
							default:
								tmpRegexStr = ".*"
										+ specialEscape(uc.getRegexValue())
										+ ".*";
								break;
							}
							criteria.and(s).regex(tmpRegexStr);
						}
						hasParm = true;
					}

					if (null != uc.getDirOrderBy()) {
						switch (uc.getDirOrderBy()) {
						case 0:
							query.sort().on(s, Order.ASCENDING);
							break;
						case 1:
							query.sort().on(s, Order.DESCENDING);
							break;
						default:
							query.sort().on(s, Order.ASCENDING);
							break;
						}
					}
				} else {
					if (map.get(s).toString().length() > 0) {
						criteria.and(s).is(map.get(s));
						hasParm = true;
					}
				}
			}
		}

		if (handlerKeyRepeat(criteria, repeatMaps) || hasParm) {
			query.addCriteria(criteria);
		}
		return query;
	}

	static boolean checkIsRepeat(String key) {
		if (key.contains(IUtilCondtion.ENUM_REPEAT)) {
			return true;
		}
		return false;
	}

	static void mergeList(Map<String, List<Object>> maps, String key, Object obj) {
		if (null == obj) {
			return;
		}
		List<Object> lus = maps.get(key);
		if (null == lus) {
			lus = new ArrayList<Object>();
		}
		lus.add(obj);
		maps.put(key, lus);
	}

	static boolean handlerKeyRepeat(Criteria parentCriteria,
			Map<String, List<Object>> maps) {
		boolean hasParm = false;
		Criteria criteria = null;
		for (String key : maps.keySet()) {
			List<Object> list = maps.get(key);
			Criteria tc = createCriteria(key, list);
			if (tc != null) {
				hasParm = true;
				if (criteria == null) {
					criteria = tc;
					continue;
				}
				criteria = tc.andOperator(criteria);
			}
		}
		if (hasParm) {
			parentCriteria.andOperator(criteria);
		}
		return hasParm;
	}

	static Criteria createCriteria(String key, List<Object> list) {
		boolean hasParm = false;
		Criteria criteria = new Criteria();
		criteria = criteria.and(key);
		for (Object obj : list) {
			if (obj instanceof UtilCondtion) {
				UtilCondtion uc = (UtilCondtion) obj;
				if (null != uc.getDirCompare()) {
					hasParm = true;
					switch (uc.getDirCompare()) {
					case 0:
						criteria.is(uc.getCompareValue());
						break;
					case 1:
						criteria.lt(uc.getCompareValue());
						break;
					case 2:
						criteria.gt(uc.getCompareValue());
						break;
					case 3:
						criteria.gte(uc.getCompareValue());
						break;
					case 4:
						criteria.lte(uc.getCompareValue());
						break;
					default:
						criteria.is(uc.getCompareValue());
						break;
					}
				}
				if (null != uc.getRegexValue()
						&& uc.getRegexValue().length() > 0) {
					if (null == uc.getDirRegex()) {
						criteria.is(uc.getRegexValue());
					} else {
						String tmpRegexStr = ".*"
								+ specialEscape(uc.getRegexValue()) + ".*";
						switch (uc.getDirRegex()) {
						case 0:
							tmpRegexStr = ".*"
									+ specialEscape(uc.getRegexValue());
							break;
						case 1:
							tmpRegexStr = ".*"
									+ specialEscape(uc.getRegexValue()) + ".*";
							break;
						case 2:
							tmpRegexStr = specialEscape(uc.getRegexValue())
									+ ".*";
							break;
						default:
							tmpRegexStr = ".*"
									+ specialEscape(uc.getRegexValue()) + ".*";
							break;
						}
						criteria.regex(tmpRegexStr);
					}
					hasParm = true;
				}
			} else {
				criteria.is(obj);
				hasParm = true;
			}
		}
		if (hasParm) {
			return criteria;
		}
		return null;
	}

	public List<T> queryEntitysByQuery(MongoTemplate mongoTemplate,
			Query query, int skip, int limit, Class<T> entityClass) {
		return mongoTemplate.find(query.skip(skip).limit(limit), entityClass);
	}

	public List<T> queryEntitysByQuery(MongoOperations mongoTemplate,
			Query query, int skip, int limit, Class<T> entityClass,
			String tableName) {
		return mongoTemplate.find(query.skip(skip).limit(limit), entityClass,
				tableName);
	}

	public static Long queryCountByQuery(MongoTemplate mongoTemplate,
			Query query, Class<?> entityClass) {
		return mongoTemplate.count(query, entityClass);
	}

	public static long queryCountByQuery(MongoOperations mongoTemplate,
			Query query, String tableName) {
		return mongoTemplate.count(query, tableName);
	}

	public PaginatedArrayList<T> getPaginatedArrayListByConditions(
			MongoTemplate mongoTemplate, PaginatedQueryCondition<T> condition,
			Class<T> className) {
		Query query = takeQueryByCondtion(condition.getCondition());
		PaginatedArrayList<T> list = new PaginatedArrayList<T>(
				condition.getCurrentPageNum(), condition.getPageSize());
		Long count = queryCountByQuery(mongoTemplate, query, className);
		if (null != count)
			list.setTotalItem(Integer.valueOf(count.toString()));
		List<T> dqs = queryEntitysByQuery(mongoTemplate, query,
				list.getStartRow() > 0 ? list.getStartRow() - 1 : 0,
				list.getPageSize(), className);
		for (T t : dqs) {
			list.add(t);
		}
		return list;
	}

	public PaginatedArrayList<T> getPaginatedArrayListByConditions(
			MongoOperations mongoTemplate,
			PaginatedQueryCondition<T> condition, Class<T> className,
			String tableName) {
		Query query = takeQueryByCondtion(condition.getCondition());
		PaginatedArrayList<T> list = new PaginatedArrayList<T>(
				condition.getCurrentPageNum(), condition.getPageSize());
		long count = queryCountByQuery(mongoTemplate, query, tableName);
		list.setTotalItem((int) count);
		List<T> dqs = queryEntitysByQuery(mongoTemplate, query,
				list.getStartRow() > 0 ? list.getStartRow() - 1 : 0,
				list.getPageSize(), className, tableName);
		for (T t : dqs) {
			list.add(t);
		}
		return list;
	}

	static String specialEscape(final String source) {
		return source.replace("\\", "\\\\\\").replace("/", "\\/")
				.replace("!", "\\!").replace("@", "\\@").replace("#", "\\#")
				.replace("$", "\\\\$").replace("%", "\\%")
				.replace("^", "\\\\^").replace("&", "\\&")
				.replace("*", "\\\\*").replace("+", "\\\\+")
				.replace("(", "\\(").replace(")", "\\)").replace("?", "\\\\?")
				.replace("|", "\\|").replace("'", "\\'").replace(":", "\\:")
				.replace("<", "\\<").replace(">", "\\>").replace(".", "\\\\.");
	}
}
