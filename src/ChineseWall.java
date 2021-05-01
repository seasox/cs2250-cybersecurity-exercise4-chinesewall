import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChineseWall
{
	/**
	 * Contains the current test case data.
	 */
	static InputFileParser _testcaseParser;

	/** The requested companies per subject, used for locking objects in the same COI class */
	private static Map<Integer, Set<Integer>> requestedCompaniesForSubject;

	/**
	 * Program entry point.
	 * 
	 * @param args
	 *            Ignored.
	 */
	public static void main(String[] args)
	{
		// Read test case from stdin
		_testcaseParser = new InputFileParser(System.in);

		requestedCompaniesForSubject = new HashMap<>();

		// NOTE Be careful with integer comparisons:
		//      The "==" operator only works with variables of type "int", use Integer.equals for all others

		// Run through access requests and check each of them
		boolean denied = false;
		for(InputFileParser.ObjectAccessRequest objectAccessRequest : _testcaseParser.ObjectAccessRequests)
		{
			// Access denied?
			if(!handleAccessRequest(objectAccessRequest.SubjectId, objectAccessRequest.ObjectId))
			{
				System.out.println("Access denied for request (" + objectAccessRequest.SubjectId + ", " + objectAccessRequest.ObjectId + ")");
				denied = true;
			}
		}
		if(!denied)
			System.out.println("All access requests were granted!");
	}

	/**
	 * Checks the given object access requests and returns whether it is granted or
	 * not; this method also keeps track of past object accesses.
	 * 
	 * @param subjectId
	 *            The ID of the subject requesting the object access.
	 * @param objectId
	 *            The object being accessed.
	 * @return Whether the object access is granted or denied.
	 */
	public static boolean handleAccessRequest(Integer subjectId, Integer objectId)
	{
		Set<Integer> companies = requestedCompaniesForSubject.getOrDefault(subjectId, new HashSet<>());
		Integer requestedCompany = _testcaseParser.ObjectCompanies.get(objectId);
		Integer objectCoi = _testcaseParser.CompanyCois.get(requestedCompany);
		// check previously granted COI classes
		for (Integer grantedCompany: companies) {
			// access to this company has been granted by a previous access. we good
			if (requestedCompany.equals(grantedCompany)) {
				break;
			}
			// the requested company does have the same COI class as ya previousl granted company. Deny access
			if (_testcaseParser.CompanyCois.get(grantedCompany).equals(objectCoi)) {
				return false;
			}
		}
		companies.add(requestedCompany);
		requestedCompaniesForSubject.put(subjectId, companies);
		return true;
	}
}
