package com.itcbusiness.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.itcbusiness.entity.AuditedItem;
import com.itcbusiness.entity.CategoryDivision;
import com.itcbusiness.entity.DistributorOrders;
import com.itcbusiness.entity.DistributorsItem;
import com.itcbusiness.entity.DistributorsItemDetails;
import com.itcbusiness.entity.Inventory;
import com.itcbusiness.entity.ItcUser;
import com.itcbusiness.entity.LiabilityData;
import com.itcbusiness.entity.MendSheetData;
import com.itcbusiness.entity.PlanningData;
import com.itcbusiness.entity.WdAuthentication;
import com.itcbusiness.entity.WdInvoice;
import com.itcbusiness.model.AuditedItemModel;
import com.itcbusiness.model.DistributorOrderModel;
import com.itcbusiness.model.DistributorsItemDetailsModel;
import com.itcbusiness.model.DistributorsItemModel;
import com.itcbusiness.model.InventoryModel;
import com.itcbusiness.model.LiabilityDataModel;
import com.itcbusiness.model.PlanningDataModel;
import com.itcbusiness.model.SubProductInventoryModel;
import com.itcbusiness.model.TransientDataModel;
import com.itcbusiness.model.request.SignUpRequestModel;
import com.itcbusiness.model.request.WdInvoiceRequest;
import com.itcbusiness.model.response.LoginResponseModel;
import com.itcbusiness.model.response.MendSheetResponse;
import com.itcbusiness.model.response.SignUpResponseModel;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MapSturtMapper {

	@Mapping(target = "userCode", expression = "java(assignAnStringValue(userCode))")
	@Mapping(target = "role", expression = "java(assignAnStringValue(role))")
	ItcUser itcUserSignUpRequestToItcUser(SignUpRequestModel userData, String role, String userCode);

	@Mapping(target = "accessToken", expression = "java(assignAnStringValue(token))")
	@Mapping(target = "status", expression = "java(assignAnStringValue(\"success\"))")
	LoginResponseModel itcUserToLoginResponseMode(ItcUser itcUserData, String token);

	default String assignAnStringValue(String str) {
		return str;
	}

	@Mapping(target = "status", expression = "java(assignAnStringValue(\"success\"))")
	SignUpResponseModel itcUserToItcUserSignUpResponse(ItcUser saveAndFlush);

	Inventory inventoryModelToInventory(InventoryModel inventoryData);

	@Mapping(target = "orderCode", expression = "java(generateCode())")
	@Mapping(target = "liability", ignore = true) // Ignoring if it's not provided in model
	DistributorOrders distributorsOrderModelToDistributorsorder(DistributorOrderModel distributorOrderData);

	default String generateCode() {
		String newSt = LocalDateTime.now().toString();
		return newSt.substring(0, 4) + newSt.substring(5, 7) + newSt.substring(8, 13) + newSt.substring(14, 16)
				+ newSt.substring(17, 19);
	}

	List<SubProductInventoryModel> inventoryToSubProductInventoryModel(List<Inventory> subProductData);

	LiabilityData liabilityModelToLibility(LiabilityDataModel liabilityData);

	PlanningData planingModeToPlanning(PlanningDataModel planningData);

	@Mapping(target = "status", expression = "java(assignAnStringValue(\"success\"))")
	List<SignUpResponseModel> listItcUserToListSignUpResponseModel(List<ItcUser> reponseData);

	@Mapping(target = "auditor", ignore = true) // Ignore the role field
	TransientDataModel liabilityDataToTransientDataModel(LiabilityData data1);

//	@Mapping(target = "liability", ignore = true)
	DistributorsItem liabilityDataModelToDistributorsItem(LiabilityDataModel item);

	// @Mapping(target = "mCodesAuditedValue", ignore = true)
	AuditedItem auditedItemModelToAuditedItem(AuditedItemModel auditedItemData);

	List<DistributorsItem> listOfDistributorsItemmodelToListOfDistributorItem(
			List<DistributorsItemModel> distributorData);

	DistributorsItemDetails DistributorsItemDetailsModelToDistributorsItemDetails(DistributorsItemDetailsModel item);

	LiabilityData distributorsItemDetailsModelToLiabilityData(DistributorsItemDetailsModel item);

	@Mapping(target = "category", expression = "java(assignAnStringValue(key))")
	@Mapping(target = "division", expression = "java(assignAnStringValue(item))")
	CategoryDivision valuesToCategoryDivision(String key, String item);

	DistributorsItem distributorsItemModelToDistributorItem(DistributorsItemModel item);

	MendSheetResponse mendSheetDataToMendSheetResponse(MendSheetData item);

	DistributorsItemDetails distributorsItemDetailsDataTodistributorsItemDetails(DistributorsItemDetailsModel item);

	DistributorsItem LiabilityEntityToDistributorItemEntity(LiabilityData value);

	// @Mapping(target = "liability", ignore = true)
	WdInvoice wdInvoiceDataToWdInvoice(WdInvoiceRequest invoiceData);

	@Mapping(target = "wdCode", expression = "java(assignAnStringValue(wdCode))")
	@Mapping(target = "apiKey", expression = "java(assignAnStringValue(apiKey))")
	WdAuthentication wdCodeAndApiKeytoWdAuthentiication(String wdCode, String apiKey);

//	@Mapping(target = "auditor", expression = "java(setMapValue(auditor))")
//	@Mapping(target = "planning", expression = "java(assignAnStringValue(planning))")
//	@Mapping(target = "remaining", expression = "java(assignAnStringValue(remaining))")
//	@Mapping(target = "todayDate", expression = "java(setLocalDate(todayDate))")
//	@Mapping(target = "years", source = "liabilityData.years") // Assuming years is from LiabilityData
//	TransientDataModel liabilityAndPlanningDataToTransientDataModel(LiabilityData liabilityData,
//			Map<String, List<String>> auditor, String planning, String remaining, LocalDate todayDate);
//
//	default Map<String, List<String>> setMapValue(Map<String, List<String>> map) {
//		return map;
//	}
//
//	default LocalDate setLocalDate(LocalDate date) {
//		return date;
//	}

//	@Mapping(target = "productName", expression = "java(assignValue(productName))")
//	@Mapping(target = "productCode", expression = "java(assignValue(productCode))")
//	@Mapping(target = "subProduct", expression = "java(putObject(subProductData1))")
//	ProductInventoryModel proNameAndCodeAndListProToProInventoryModel(String productName, String productCode,
//			List<SubProductInventoryModel> subProductData1);

}
