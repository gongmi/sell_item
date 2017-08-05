package com.gm.web;

import com.gm.dto.Exposer;
import com.gm.dto.KillRecord;
import com.gm.dto.Result;
import com.gm.entity.Product;
import com.gm.enums.StateEnum;
import com.gm.exception.RepeatKillException;
import com.gm.exception.KillCloseException;
import com.gm.service.KillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/product")//url:模块/资源/{}/细分
public class KillController
{
    @Autowired
    private KillService killService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model)
    {
        //list.jsp+mode=ModelAndView
        //获取列表页
        List<Product> products= killService.getProductList();
        model.addAttribute("products",products);
        return "list";
    }

    @RequestMapping(value = "/{productId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("productId") Long productId, Model model)
    {
        if (productId == null)
        {
            return "redirect:/product/list";
        }

        Product product = killService.getById(productId);
        if (product ==null)
        {
            return "forward:/product/list";
        }

        model.addAttribute("product", product);

        return "detail";
    }

    //ajax ,json暴露秒杀接口的方法
    @RequestMapping(value = "/{productId}/exposer",
                    method = RequestMethod.GET,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result<Exposer> exposer(@PathVariable("productId") Long productId)
    {
        Result<Exposer> result;
        try{
            Exposer exposer= killService.exportKillUrl(productId);
            result=new Result<Exposer>(true,exposer);
        }catch (Exception e)
        {
            e.printStackTrace();
            result=new Result<Exposer>(false,e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/{productId}/{md5}/kill",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result<KillRecord> execute(@PathVariable("productId") Long productId,
                                      @PathVariable("md5") String md5,
                                      @CookieValue(value = "userPhone",required = false) Long userPhone)
    {
        if (userPhone==null)
        {
            return new Result<KillRecord>(false,"未注册");
        }
        Result<KillRecord> result;

        try {
            KillRecord record = killService.kill(productId, userPhone, md5);
            return new Result<KillRecord>(true, record);
        }catch (RepeatKillException e1)
        {
            KillRecord record=new KillRecord(productId, StateEnum.REPEAT_KILL);
            return new Result<KillRecord>(true,record);
        }catch (KillCloseException e2)
        {
            KillRecord record=new KillRecord(productId, StateEnum.END);
            return new Result<KillRecord>(true,record);
        }
        catch (Exception e)
        {
            KillRecord record=new KillRecord(productId, StateEnum.INNER_ERROR);
            return new Result<KillRecord>(true,record);
        }

    }

    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> time()
    {
        Date now=new Date();
        return new Result<Long>(true,now.getTime());
    }
}























