package com.kyu.gabriel.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kyu.gabriel.core.model.dto.BanDTO;
import com.kyu.gabriel.core.model.dto.ListBanDTO;
import com.kyu.gabriel.core.model.dto.UserDTO;
import com.kyu.gabriel.core.model.po.user.BanRecord;
import com.kyu.gabriel.core.model.vo.ListBanRecordVO;
import com.kyu.gabriel.core.model.vo.ListUserVO;
import com.kyu.gabriel.core.security.Encryptor;
import com.kyu.gabriel.core.string.StringGenerator;
import com.kyu.gabriel.core.string.StringUtils;
import com.kyu.gabriel.user.dao.BanDao;
import com.kyu.gabriel.core.model.po.user.User;
import com.kyu.gabriel.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;
    private final BanDao banDao;
    private final Encryptor encryptor = Encryptor.getInstance();

    @Autowired
    public UserService(UserDao userDao, BanDao banDao){
        this.userDao = userDao;
        this.banDao = banDao;
    }

    public boolean add(User user){
        return user.insertOrUpdate();
    }

    public User getUserByUsername(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return new User().selectOne(wrapper);
    }

    public User getUserEmail(String email){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        return new User().selectOne(wrapper);
    }

    public User getUserByUUID(String uuid){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid);
        return new User().selectOne(wrapper);
    }

    public User getUserByMobilePhone(String mobile){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        return new User().selectOne(wrapper);
    }

    public User getUserByUniqueColumns(String column){
        return userDao.selectUserByUniqueColumns(column);
    }

    public boolean update(User user){
        return user.updateById();
    }

    public boolean delete(User user){
        if (user == null){
            return false;
        }
        return user.deleteById();
    }

    public IPage<UserDTO> selectUsers(ListUserVO listUserVo){
        Page<User> pageInfo = new Page<>(listUserVo.getPage(), listUserVo.getLimit());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(listUserVo.getUsername())){
            wrapper.like("username",  listUserVo.getUsername());
        }
        if (!StringUtils.isEmpty(listUserVo.getEmail())){
            wrapper.eq("email", listUserVo.getEmail());
        }
        if (!StringUtils.isEmpty(listUserVo.getMobile())){
            wrapper.eq("mobile", listUserVo.getMobile());
        }
        if (!StringUtils.isEmpty(listUserVo.getNick())){
            wrapper.like("nick", listUserVo.getNick());
        }
        if (!StringUtils.isEmpty(listUserVo.getRegDateStart()) && !StringUtils.isEmpty(listUserVo.getRegDateEnd())){
            wrapper.between("register_date", listUserVo.getRegDateStart(), listUserVo.getRegDateEnd());
        }
        if (!StringUtils.isEmpty(listUserVo.getLoginDateStart()) && !StringUtils.isEmpty(listUserVo.getLoginDateEnd())){
            wrapper.between("last_login_date", listUserVo.getLoginDateStart(), listUserVo.getLoginDateEnd());
        }
        if (listUserVo.getBanned() != null){
            wrapper.eq("banned", listUserVo.getBanned());
        }
        wrapper.orderByDesc("register_date");
        return userDao.listUserForAdmin(pageInfo, wrapper);
    }

    public int userCount(){
        return userDao.selectCount(new QueryWrapper<>());
    }

    public String resetUserPassword(String uuid){
        User user = userDao.selectById(uuid);
        if (user == null){
            return null;
        }
        String newPassword = StringGenerator.randomStr(10, true);
        user.setPassword(encryptor.generatePassword(uuid, newPassword));
        if (user.updateById()){
            return newPassword;
        }
        return null;
    }

    @Transactional
    public boolean setUserStatus(BanDTO dto){
        System.out.println(dto);
        if (!dto.isStatus()){
            QueryWrapper<BanRecord> wrapper = new QueryWrapper<>();
            wrapper.eq("uuid", dto.getUuid())
                    .ne("delete_flag", 1);
            List<BanRecord> banRecords = banDao.selectList(wrapper);
            banRecords.forEach(banRecord -> {
                banRecord.setDeleteFlag(true);
                banRecord.setDeleteDate(new Date());
                if (dto.getOperator() != null){
                    banRecord.setOperator(dto.getOperator().getUuid());
                }
                banDao.updateById(banRecord);
            });
            return true;
        } else {
            BanRecord record = new BanRecord()
                    .setUuid(dto.getUuid())
                    .setStartDate(dto.getStartDate())
                    .setEndDate(dto.getEndDate())
                    .setReason(dto.getReason());
            if (dto.getOperator() != null){
                record.setOperator(dto.getOperator().getUuid());
            }
            return record.insertOrUpdate();
        }
    }

    public BanRecord getBanRecordByUUid(String uuid){
        QueryWrapper<BanRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("uuid", uuid)
                .ne("delete_flag", 1)
                .gt("end_date", new Date());
        return banDao.selectOne(wrapper);
    }

    public IPage<ListBanDTO> listBanRecords(ListBanRecordVO vo){
        Page<BanRecord> pageInfo = new Page<>(vo.getPage(), vo.getLimit());
        QueryWrapper<BanRecord> wrapper = new QueryWrapper<>();
        if (vo.getDeleteFlag() != null){
            wrapper.eq("b.delete_flag", vo.getDeleteFlag());
        }
        if (!StringUtils.isEmpty(vo.getUsername())){
            wrapper.like("u2.username", vo.getUsername());
        }
        if (!StringUtils.isEmpty(vo.getOperator())){
            wrapper.and(i -> i.like("u.username", vo.getOperator()).or().eq("u.uuid", vo.getOperator()));
        }
        if (!StringUtils.isEmpty(vo.getReason())){
            wrapper.like("b.reason", vo.getReason());
        }
        if (!StringUtils.isEmpty(vo.getUuid())){
            wrapper.eq("b.uuid", vo.getUuid());
        }
        if (!StringUtils.isEmpty(vo.getRelieveDateStart()) && !StringUtils.isEmpty(vo.getRelieveDateEnd())){
            wrapper.between("b.end_date", vo.getRelieveDateStart(), vo.getRelieveDateEnd());
        }
        wrapper.orderByDesc("b.start_date");
        return banDao.listBanRecord(pageInfo, wrapper);
    }

    public boolean relieve(int id, User operator){
        BanRecord ban = banDao.selectById(id);
        if (ban != null){
            ban.setDeleteFlag(true);
            ban.setDeleteDate(new Date());
            ban.setOperator(operator.getUuid());
            return ban.updateById();
        }
        return false;
    }

    @Transactional
    public int relieveBatch(List<Integer> ids, User operator){
        List<BanRecord> records = banDao.selectBatchIds(ids);
        records.forEach(record -> {
            record.setDeleteFlag(true);
            record.setDeleteDate(new Date());
            record.setOperator(operator.getUuid());
        });
        return banDao.updateBatch(records);
    }

    public boolean deleteBanRecord(int id){
        BanRecord banRecord = banDao.selectById(id);
        if (banRecord != null){
            return banRecord.deleteById();
        }
        return false;
    }

    public int deleteBanRecordBatch(List<Integer> ids){
        return banDao.deleteBatchIds(ids);
    }
}
