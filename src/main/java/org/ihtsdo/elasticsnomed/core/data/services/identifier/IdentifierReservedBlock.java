package org.ihtsdo.elasticsnomed.core.data.services.identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.ihtsdo.elasticsnomed.core.data.domain.ComponentType;
import org.ihtsdo.elasticsnomed.core.data.services.RuntimeServiceException;

public class IdentifierReservedBlock {

	private Map<ComponentType, Queue<String>> idsReserved = new HashMap<>();
	private Map<ComponentType, Queue<String>> idsAssigned = new HashMap<>();

	// @PGW - This is never used
	private Set<ComponentType> alreadyRegistered = new HashSet<>();
	
	IdentifierReservedBlock() {
		for (ComponentType componentType : ComponentType.values()) {
			idsReserved.put(componentType, new LinkedList<>());
			idsAssigned.put(componentType, new LinkedList<>());
		}
	}

	public String getId(ComponentType componentType) {
		String id = idsReserved.get(componentType).poll();
		
		if (id == null) {
			throw new RuntimeServiceException ("Unexpected request for identifier of type " + componentType);
		}
		
		if (!alreadyRegistered.contains(componentType)) {
			idsAssigned.get(componentType).add(id);
		}
		return id;
	}
	
	void addId(ComponentType componentType, String sctId) {
		idsReserved.get(componentType).add(sctId);
	}

	public void addAll(ComponentType componentType, List<String> sctIds) {
		idsReserved.get(componentType).addAll(sctIds);
	}

	Collection<String> getIdsAssigned(ComponentType componentType) {
		return idsAssigned.get(componentType);
	}
	
	public int size(ComponentType componentType) {
		return idsReserved.get(componentType).size();
	}
}