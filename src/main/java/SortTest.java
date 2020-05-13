/**
 * @Author binz.zhang
 * @Date: 2020-05-12    18:39
 */
public class SortTest {

    public static void main(String[] args) {
        int [] num1  = {1,2,7,0,0,0,0};
        int [] num2  = {2,5,6};
        int m = 3;
        int n = 2;
        int [] res =  new int[m+n];
        int r = 0;
        int l = 0;
        int i = 0;
        while (r<m && l <n){
            if(num1[r]<num2[l]){
                res[i++] = num1[r];
                r++;
            }else {
                res[i++] = num2[l];
                l++;
            }
        }
        while (r<m){
            res[i++] = num1[r];
        }
        while (l<n){
            res[i++] = num2[l];
        }
        for (int x:res){
            System.out.println(x);
        }

    }


}
