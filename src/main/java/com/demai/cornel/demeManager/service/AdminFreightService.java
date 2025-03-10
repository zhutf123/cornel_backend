package com.demai.cornel.demeManager.service;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.demai.cornel.dao.DryTowerDao;
import com.demai.cornel.demeManager.model.FreightExInfo;
import com.demai.cornel.demeManager.model.FreightWithToLocation;
import com.demai.cornel.demeManager.vo.*;
import com.demai.cornel.purcharse.dao.FreightInfoMapper;
import com.demai.cornel.purcharse.dao.LocationInfoMapper;
import com.demai.cornel.purcharse.model.FreightInfo;
import com.demai.cornel.purcharse.model.LocationInfo;
import com.demai.cornel.purcharse.model.TransportType;
import com.demai.cornel.util.CookieAuthUtils;
import com.demai.cornel.util.JacksonUtils;
import com.demai.cornel.util.TimeStampUtil;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.hp.gagawa.java.elements.A;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TransferQueue;

/**
 * @Author binz.zhang
 * @Date: 2020-04-03    16:27
 */
@Service @Slf4j public class AdminFreightService {
    @Resource private FreightInfoMapper freightInfoMapper;
    @Resource private DryTowerDao dryTowerDao;
    @Resource private LocationInfoMapper locationInfoMapper;

//    @PostConstruct public void init() {
//        List<LocationInfo> locationInfos = dryTowerDao.getLocation();
//        locationInfos.stream().forEach(x -> {
//
//            if (x.getLocationId() == null) {
//                x.setLocationId(UUID.randomUUID().toString());
//                locationInfoMapper.insertSelective(x);
//
//            }
//
//        });
//
//    }

    public List<AdminGetFreightViewResp> adminGetFreightView(Integer offset, Integer pgSize) {
        List<AdminGetFreightViewResp> freightViewResps = dryTowerDao.adminGetDryTower(pgSize, offset);
        if (freightViewResps == null) {
            return Collections.EMPTY_LIST;
        }
        if (freightViewResps == null) {
            return Collections.emptyList();
        }
        freightViewResps.stream().forEach(x -> {
            AdminGetFreightViewResp adminGetFreightViewResp = freightInfoMapper
                    .adminnGetOptFreightView(x.getLocationId());
            x.setAverPrice((adminGetFreightViewResp == null || adminGetFreightViewResp.getAverPrice() == null) ?
                    new BigDecimal(0) :
                    adminGetFreightViewResp.getAverPrice());
            x.setMinPrice((adminGetFreightViewResp == null || adminGetFreightViewResp.getMinPrice() == null) ?
                    new BigDecimal(0) :
                    adminGetFreightViewResp.getMinPrice());
        });
        return freightViewResps;
    }

    public List<AdminLocationMode> getSystemLocationList(Integer offset, Integer pgSize) {
        List<AdminLocationMode> locationModes = locationInfoMapper
                .getSystemLocation(Optional.ofNullable(offset).orElse(0), Optional.ofNullable(pgSize).orElse(10));
        return locationModes == null ? Collections.EMPTY_LIST : locationModes;
    }

    public AdminAddLocationResp adminAddLocation(AdminLocationMode adminLocationMode) {
        if (adminLocationMode == null || Strings.isNullOrEmpty(adminLocationMode.getLocationArea()) || Strings
                .isNullOrEmpty(adminLocationMode.getLocationDetail())) {
            return AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.PARAM_ERROR.getValue())
                    .build();
        }
        adminLocationMode.setLocation(Strings.isNullOrEmpty(adminLocationMode.getLocation()) ?
                adminLocationMode.getLocationArea() + adminLocationMode.getLocationDetail() :
                adminLocationMode.getLocation());

        if (!Strings.isNullOrEmpty(adminLocationMode.getLocationId())) {

            return locationInfoMapper.updateLocationInfoByLocationId(adminLocationMode.getLocationArea(),
                    adminLocationMode.getLocationDetail(), adminLocationMode.getLocation(),
                    adminLocationMode.getLocationId()) == 1 ?
                    AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.SUCCESS.getValue())
                            .build() :
                    AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.SERVER_ERR.getValue())
                            .build();
        }

        AdminLocationMode alread = locationInfoMapper
                .selectLocationModelByInfo(adminLocationMode.getLocationArea(), adminLocationMode.getLocationDetail(),
                        adminLocationMode.getLocation());
        if (alread != null && alread.getSystemFlag() != null && alread.getSystemFlag().equals(1)) {
            return AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.SUCCESS.getValue())
                    .locationId(alread.getLocationId()).build();
        }
        adminLocationMode.setSystemFlag(1);
        adminLocationMode.setStatus(1);
        if (alread != null) {
            adminLocationMode.setLocationId(alread.getLocationId());
            if (locationInfoMapper.insertSelectiveAdminLocationMode(adminLocationMode) != 1) {
                return AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.PARAM_ERROR.getValue())
                        .build();
            }
            return AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.SUCCESS.getValue())
                    .locationId(alread.getLocationId()).build();
        }
        adminLocationMode.setLocationId(UUID.randomUUID().toString());
        if (locationInfoMapper.insertSelectiveAdminLocationMode(adminLocationMode) != 1) {
            return AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.SERVER_ERR.getValue())
                    .build();
        }
        return AdminAddLocationResp.builder().optStatus(AdminAddLocationResp.STATUS_ENUE.SUCCESS.getValue())
                .locationId(adminLocationMode.getLocationId()).build();

    }

    public List<String> getTransport() {
        List<String> type = new ArrayList<>();
        Arrays.stream(TransportType.TRANSPORT_TYPE_ENUM.values()).forEach(x -> {
            type.add(x.getExpr());
        });
        return type;
    }

    public List<AdminGetFreTypeResp> getFreightInfo() {
        List<AdminGetFreTypeResp> typeResps = new ArrayList<>();
        Arrays.stream(FreightExInfo.TYPE_ENUE.values()).forEach(x -> {
            typeResps.add(new AdminGetFreTypeResp(x.getValue(), "", x.getType()));
        });
        return typeResps;
    }

    public AdminGetFreightResp adminGetFreight(String towerId) {
        AdminGetFreightResp rest = new AdminGetFreightResp();
        AdminGetFreightViewResp viewResp = dryTowerDao.adminGetDryTowerFreiViewByTowerId(towerId);
        if (viewResp == null) {
            return null;
        }
        rest.setTowerId(viewResp.getTowerId());
        rest.setLocationId(viewResp.getLocationId());
        rest.setLocation(viewResp.getLocation());
        BeanUtils.copyProperties(viewResp, rest);
        AdminGetFreightViewResp simpleInfo = freightInfoMapper.adminnGetOptFreightView(rest.getLocationId());

        if (simpleInfo == null) {
            rest.setAverPrice(new BigDecimal(0));
            rest.setMinPrice(new BigDecimal(0));
            rest.setDestinationList(Collections.EMPTY_LIST);
            return rest;
        } else {
            rest.setAverPrice(simpleInfo.getAverPrice());
            rest.setMinPrice(simpleInfo.getMinPrice());
        }
        List<FreightWithToLocation> ad = freightInfoMapper.adminGetFreightWithToLocaByFromLoc(viewResp.getLocationId());
        if (ad == null) {
            rest.setDestinationList(Collections.EMPTY_LIST);
            return rest;
        }
        HashMap<String, AdminGetFreightResp.FreightDetailInfo> mapRest = new HashMap<>();
        ad.stream().forEach(x -> {
            AdminGetFreightResp.TransportList temp = new AdminGetFreightResp.TransportList();
            temp.setFreightId(x.getFreightId());
            temp.setTransportType(buildTransport(x.getTransportType()));
            temp.setTotalPrice(x.getPrice());
            temp.setUpdateFlag(
                    TimeStampUtil.timeStampConvertString("yyyy-MM-dd", new Timestamp(System.currentTimeMillis()))
                            .equals(x.getUpdateTime()) ? 1 : 0);
            temp.setUpdateTime(x.getUpdateTime());
            temp.setExInfo(Strings.isNullOrEmpty(x.getExInfo()) ?
                    Collections.EMPTY_LIST :
                    JSONObject.parseArray(x.getExInfo(), FreightExInfo.class));

            if (mapRest.containsKey(x.getToLocation())) {
                mapRest.get(x.getToLocation()).getTransportList().add(temp);
            } else {
                AdminGetFreightResp.FreightDetailInfo freightDetailInfo = new AdminGetFreightResp.FreightDetailInfo();
                freightDetailInfo.setToLocationId(x.getToLocation());
                freightDetailInfo.setToLocation(x.getToLocationTx());
                freightDetailInfo.setTransportList(Lists.newArrayList(temp));
                mapRest.put(x.getToLocation(), freightDetailInfo);
            }
        });
        rest.setDestinationList(mapRest.values());
        return rest;

    }

    public AdminOperResp updateFreightInfo(AdminUpdateFreightReq adminUpdateFreightReq) {

        log.debug("updateFreightInfo pram is {}", JacksonUtils.obj2String(adminUpdateFreightReq));
        if (adminUpdateFreightReq == null) {
            return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        if (Strings.isNullOrEmpty(adminUpdateFreightReq.getTowerId())
                || adminUpdateFreightReq.getDestinationList() == null) {
            return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        AdminGetFreightViewResp viewResp = dryTowerDao
                .adminGetDryTowerFreiViewByTowerId(adminUpdateFreightReq.getTowerId());
        if (viewResp == null) {
            return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.PARAM_ERROR.getValue()).build();
        }
        Set<String> fromFreight = freightInfoMapper.selectFreightIdByFromLocation(viewResp.getLocationId());
        if (fromFreight == null) {
            fromFreight = new HashSet<>(0);
        }
        Set<String> finalFromFreight = fromFreight;
        adminUpdateFreightReq.getDestinationList().stream().forEach(x -> {
            List<String> freight = new ArrayList<>();
            if (x.getTransportList() != null) {
                for (AdminUpdateFreightReq.TransportList transportList : x.getTransportList()) {
                    FreightInfo freightInfo = new FreightInfo();
                    freightInfo.setPrice(transportList.getTotalPrice());
                    freightInfo.setFromLocation(viewResp.getLocationId());
                    LocationInfo fromLocationInfo  = locationInfoMapper.selectByLocationId(viewResp.getLocationId());
                    LocationInfo toLocationInfo  = locationInfoMapper.selectByLocationId(x.getToLocationId());

                    freightInfo.setToLocation(x.getToLocationId());
                    freightInfo.setTransportType(convertTranSportToSet(transportList.getTransportType()));
                    freightInfo.setExInfo(transportList.getExInfo() == null ?
                            null :
                            JSONObject.toJSONString(transportList.getExInfo()));
                    freightInfo.setReviewUser(CookieAuthUtils.getCurrentUser());
                    freightInfo.setFreightId(UUID.randomUUID().toString());
                    freightInfo.setFromLocationArea(fromLocationInfo.getLocationArea());
                    freightInfo.setFromLocationDetail(fromLocationInfo.getLocationDetail());
                    freightInfo.setFromLocationTex(fromLocationInfo.getLocation());
                    freightInfo.setToLocationArea(toLocationInfo.getLocationArea());
                    freightInfo.setToLocationDetail(toLocationInfo.getLocationDetail());
                    freightInfo.setToLocationTex(toLocationInfo.getLocation());
                    if (Strings.isNullOrEmpty(transportList.getFreightId())) {
                        int res = freightInfoMapper.insertSelective(freightInfo);
                    } else if (transportList.getIsUpdate() != null && transportList.getIsUpdate().equals(1)) {
                        freightInfoMapper.updateStatus(transportList.getFreightId());
                        freightInfoMapper.insertSelective(freightInfo);
                    }
                    finalFromFreight.remove(Strings.isNullOrEmpty(transportList.getFreightId()) ?
                            "" :
                            transportList.getFreightId());
                    finalFromFreight.remove(freightInfo.getFreightId());
                }
            }

        });
        if (finalFromFreight != null && finalFromFreight.size() > 0) {
            freightInfoMapper.updateFreightStatusByFreightIds(finalFromFreight);
        }
        return AdminOperResp.builder().optStatus(AdminOperResp.STATUS_ENUE.SUCCESS.getValue()).build();
    }

    public String buildTransport(Set<String> transport) {
        if (transport == null) {
            return "未知类型";
        }
        StringBuilder sb = new StringBuilder();
        transport.stream().forEach(x -> {
            TransportType.TRANSPORT_TYPE_ENUM type_enum = TransportType.typeOf(x);
            if (type_enum != null) {
                sb.append(type_enum.getExpr()).append("+");
            }
        });
        return sb == null ? "未知类型" : sb.toString().endsWith("+") ? sb.substring(0, sb.lastIndexOf("+")) : sb.toString();

    }

    public Set<String> convertTranSportToSet(String tranSportType) {
        if (Strings.isNullOrEmpty(tranSportType)) {
            return null;
        }
        List<String> typeList = Splitter.on("+").splitToList(tranSportType);
        log.debug("convertTranSportToSet type is {}", JacksonUtils.obj2String(typeList));
        if (typeList == null) {
            return null;
        }
        Set<String> type = new HashSet<>();
        typeList.stream().forEach(x -> {
            log.debug("..>>>>>>x =[{}]", x);
            type.add(TransportType.exparOf(x.trim()).getType());
        });
        return type;

    }

    public static void main(String[] args) {

        Set<String> type = new HashSet<>();
        type.add(TransportType.exparOf("汽运").getType());
        type.add("1");
        type.add("2");
        type.remove("1");
        System.out.println(JacksonUtils.obj2String(type));
    }

}
