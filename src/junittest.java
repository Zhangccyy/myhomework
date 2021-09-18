import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class junittest {
    @Test
    public void grade1_test(){
        test01 test1=new test01();
        String[] keywords={
                "int","long","short","float","double","char","unsigned",
                "signed", "const","void","volatile","enum","struct",
        };
        int i=0;
        int count=0;
        for(String word:keywords){
            test01.keymap.put(word,i);
            count++;
            i++;
        }
        test01.grade1();
        Assert.assertEquals(78,test.totalnum);
    }
    @Test
    public void grade4_5_test(){
        test01 test1=new test01();
        String[] array={"if","else","if","elseif","elseif","if","else","else","if","elseif","else"};
        for(int i=0;i<array.length;i++){
            test01.words.add(array[i]);
        }
        test01.grade4_5(4);
        Assert.assertEquals(2,test.i_e_num);
        Assert.assertEquals(2,test.i_ei_e_num);
    }
}
