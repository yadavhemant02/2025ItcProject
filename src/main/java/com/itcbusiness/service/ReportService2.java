package com.itcbusiness.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.repository.LiabilityDataRepository;

import lombok.Getter;
import lombok.Setter;

@Service
public class ReportService2 {

	private LiabilityDataRepository liabilityDataRepository;

	public ReportService2(LiabilityDataRepository liabilityDataRepository) {
		super();
		this.liabilityDataRepository = liabilityDataRepository;
	}

	public List<year> allSolution() {
		try {

			List<LiabilityData> data = this.liabilityDataRepository.findAll().stream()
					.filter(item -> !"DONE".equals(item.getPayments())).toList();

			HashMap<String, year> map = new HashMap<>();

			data.forEach(item -> {

				String key = item.getYears() + "_" + item.getBranch();

				year yearObj = map.computeIfAbsent(item.getYears(), y -> {
					year newYear = new year();
					newYear.setYears(item.getYears());
					newYear.setBranch(new ArrayList<>());
					return newYear;
				});

				branch branchObj = yearObj.getBranch().stream().filter(b -> b.getBranchs().equals(item.getBranch()))
						.findFirst().orElseGet(() -> {
							branch newBranch = new branch();
							newBranch.setBranchs(item.getBranch());
							newBranch.setMonth(new ArrayList<>());
							yearObj.getBranch().add(newBranch);
							return newBranch;
						});

				month monthObj = branchObj.getMonth().stream().filter(m -> m.getMonths().equals(item.getMonth()))
						.findFirst().orElseGet(() -> {
							month newMonth = new month();
							newMonth.setMonths(item.getMonth());
							newMonth.setCategory(new ArrayList<>());
							branchObj.getMonth().add(newMonth);
							return newMonth;
						});

				category categoryObj = monthObj.getCategory().stream().findFirst().orElseGet(() -> {

					category newCategory = new category();
					newCategory.setCg(0.0);
					newCategory.setFood(0.0);
					newCategory.setPcp(0.0);
					monthObj.getCategory().add(newCategory);
					return newCategory;
				});

				switch (item.getCategory()) {
				case "CG":
					categoryObj.setCg(categoryObj.getCg() + item.getLiability());
					break;
				case "FOOD":
					categoryObj.setFood(categoryObj.getFood() + item.getLiability());
					break;
				case "PCP":
					categoryObj.setPcp(categoryObj.getPcp() + item.getLiability());
					break;
				}
			});
			System.out.print(
					new ArrayList<>(map.values()).get(0).getBranch().get(0).getMonth().get(0).getCategory().size());
			return new ArrayList<>(map.values());
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

}

@Setter
@Getter
class year {
	private String years;
	private List<branch> branch;
}

@Setter
@Getter
class branch {
	private String branchs;
	private List<month> month;
}

@Setter
@Getter
class month {
	private String months;
	private List<category> category;
}

@Setter
@Getter
class category {
	private Double cg;
	private Double food;
	private Double pcp;
}
