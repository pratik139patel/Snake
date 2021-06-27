import java.awt.Dimension;
import java.util.Random;

public class DimStack 
{
    private Dimension[] dim_arr;
    private int Size;
    private int Obj_height_width;
    private int[] snake_path_momentum;

    public DimStack(final int size, final Dimension start_location, final int negative_obj_height_width)
    {
        Size = (size > 0) ? size : 1; //Min size is 1
        dim_arr = new Dimension[Size];

        dim_arr[0] = start_location; for(int i = 1; i < Size; ++i) { dim_arr[i] = new Dimension(negative_obj_height_width,negative_obj_height_width); }
        Obj_height_width = -1*negative_obj_height_width;
    }

    public DimStack(final int size, final int obj_height_width)
    {
        Obj_height_width = obj_height_width;
        Size = (size > 0) ? size : 1; //Min size is 1
        snake_path_momentum = new int[Size];
        dim_arr = new Dimension[Size];

        for(int i = 0; i < Size; ++i) { dim_arr[i] = new Dimension(obj_height_width*(i)*-1,0); snake_path_momentum[i] = 0; }
    }

    public boolean Push(final int x, final int y, final boolean dynamic_push)
    {
        for(Dimension D : dim_arr) { if(D.width == x && D.height == y) { return false; } }

        if(dynamic_push)
        {
            Dimension[] temp_dim_arr = new Dimension[++Size];
            temp_dim_arr[0] = new Dimension(x,y);
            for(int i = 0; i < Size-1; ++i) { temp_dim_arr[i+1] = dim_arr[i]; }
            dim_arr = temp_dim_arr;
        }
        else
        {
            for(int i = Size - 2; i >= 0; --i) { dim_arr[i+1] = dim_arr[i]; }
            dim_arr[0] = new Dimension(x,y);
        }

        return true;
    }

    public boolean smoothPush(final int x, final int y, final boolean dynamic_push, final int current_move_direction, final boolean update_snake_path_momentum, final boolean smooth_special_case_eat)
    {
        for(Dimension D : dim_arr) { if(Intersect(D,x,y,current_move_direction)) { return false; } }

        if(dynamic_push)
        {
            int[] temp_snake_path_momentum_arr = new int[++Size];
            temp_snake_path_momentum_arr[0] = current_move_direction;
            for(int i = 0; i < Size-1; ++i) {temp_snake_path_momentum_arr[i+1]=snake_path_momentum[i];}
            snake_path_momentum = temp_snake_path_momentum_arr;

            Dimension[] temp_dim_arr = new Dimension[Size];
            temp_dim_arr[0] = new Dimension(x,y);
            for(int i = 0; i < Size-1; ++i) { temp_dim_arr[i+1] = dim_arr[i]; }
            dim_arr = temp_dim_arr;
            if(!smooth_special_case_eat) {UpdateArr();}
        }

        else
        {
            if(update_snake_path_momentum) { for(int i = Size-1; i > 0; --i) {snake_path_momentum[i]=snake_path_momentum[i-1];} snake_path_momentum[0]=current_move_direction; }
            UpdateArr();
            dim_arr[0] = new Dimension(x,y);
        }

        return true;
    }

    /*
    //True if collision
    public boolean Intersect(final Dimension D, final int x, final int y, final int current_move_direction)
    {
        Dimension temp_pt = new Dimension();
        if(current_move_direction==0) { temp_pt.width = x+Obj_height_width; temp_pt.height = y+(int)(Obj_height_width/2); }
        else if(current_move_direction==1) { temp_pt.width = x+(int)(Obj_height_width/2); temp_pt.height = y+Obj_height_width; }
        else if(current_move_direction==2) { temp_pt.width = x; temp_pt.height = y+(int)(Obj_height_width/2); }
        else { temp_pt.width = x+(int)(Obj_height_width/2); temp_pt.height = y; }

        return (D.width <= temp_pt.width && temp_pt.width <= D.width+Obj_height_width && D.height <= temp_pt.height && temp_pt.height <= D.height+Obj_height_width) ? (true) : (false);
    }
    */

    //True if collision
    public boolean Intersect(final Dimension D, final int x, final int y, final int current_move_direction)
    {
        if(current_move_direction==0) { return (x+Obj_height_width-1 == D.width && y == D.height) ? (true) : (false); }
        else if(current_move_direction==1) { return (x == D.width && y+Obj_height_width-1 == D.height) ? (true) : (false); }
        else if(current_move_direction==2) { return (x-Obj_height_width+1 == D.width && y == D.height) ? (true) : (false); }
        else { return (x == D.width && y-Obj_height_width+1 == D.height) ? (true) : (false); }
    }

    public void UpdateArr()
    {
        for(int i = 1; i < Size; ++i)
        {
            if(snake_path_momentum[i] == 0) { ++dim_arr[i].width; }
            else if(snake_path_momentum[i] == 1) { ++dim_arr[i].height; }
            else if(snake_path_momentum[i] == 2) { --dim_arr[i].width; }
            else { --dim_arr[i].height; }
        }
    }

    /*
    public Dimension Spawn_Food(final Dimension window_dim)
    {
        Dimension temp_scaled_win_dim = new Dimension(window_dim.width/Obj_height_width, window_dim.height/Obj_height_width), temp_food_dim;
        if(getSizeOnScreen()+1 >= temp_scaled_win_dim.height*temp_scaled_win_dim.width) { temp_scaled_win_dim.width = -1*Obj_height_width; temp_scaled_win_dim.height = temp_scaled_win_dim.width; return temp_scaled_win_dim; }
        Random ran_obj = new Random();

        while(true)
        {
            temp_food_dim = new Dimension( (ran_obj.nextInt(temp_scaled_win_dim.width)) * Obj_height_width, (ran_obj.nextInt(temp_scaled_win_dim.height)) * Obj_height_width);
            if(!isElement(temp_food_dim)) { return temp_food_dim; }
        }
    }
    */

    public Dimension advance_spawn_food(final Dimension window_dim, final Dimension previous_food_location)
    {
        Dimension temp_scaled_win_dim = new Dimension(window_dim.width/Obj_height_width, window_dim.height/Obj_height_width);
        if(getSizeOnScreen()+1 >= temp_scaled_win_dim.height*temp_scaled_win_dim.width) { temp_scaled_win_dim.width = -1*Obj_height_width; temp_scaled_win_dim.height = temp_scaled_win_dim.width; return temp_scaled_win_dim; }

        Dimension[] play_board = new Dimension[(temp_scaled_win_dim.width*temp_scaled_win_dim.height)-(getSizeOnScreen()+1)];

        int temp_count = 0;
        Dimension temp_dim = new Dimension(0,0);
        for(int i = 0; i < window_dim.width; i += Obj_height_width)
        {
            for(int j = 0; j < window_dim.height; j += Obj_height_width)
            {
                temp_dim.width = i; temp_dim.height = j;
                if(!isElement(temp_dim) && !(temp_dim.width == previous_food_location.width && temp_dim.height == previous_food_location.height) && temp_count < play_board.length)
                {
                    play_board[temp_count] = new Dimension(temp_dim.width,temp_dim.height); ++temp_count;
                }
            }
        }

        return play_board[(new Random()).nextInt(play_board.length)];
    }

    public int getSize() { return Size; }
    public int getSizeOnScreen() { int temp_count = 0; for(Dimension D : dim_arr) { if(D.width >= 0 && D.height >= 0) {++temp_count;} } return temp_count; }

    //True if its an element, otherwise false
    public boolean isElement(final Dimension input_dim)
    {
        for(Dimension D : dim_arr) { if(D.width == input_dim.width && D.height == input_dim.height) {return true;} }
        return false;
    }
    
    public Dimension[] getDimArr(final boolean edge_correction_and_smooth_motion) 
    {
        if(edge_correction_and_smooth_motion) 
        {
            Dimension[] temp_arr = dim_arr;

            for(int i = 1; i < dim_arr.length; ++i)
            {
                if(snake_path_momentum[i] != snake_path_momentum[i-1])
                {
                    Dimension[] temp_temp_arr = temp_arr;
                    temp_arr = new Dimension[temp_arr.length+1];
                    for(int j = 0; j < temp_temp_arr.length; ++j) { temp_arr[j] = temp_temp_arr[j]; }
                    temp_arr[temp_temp_arr.length] = new Dimension( (dim_arr[i].width%Obj_height_width == 0) ? (dim_arr[i].width) : (dim_arr[i-1].width), (dim_arr[i].height%Obj_height_width == 0) ? (dim_arr[i].height) : (dim_arr[i-1].height) );
                }
            }
            
            return temp_arr;
        }

        else { return dim_arr; }
    }

    /*
    public void manipulateGraphic(Graphics graphic_obj, Color obj_color)
    {
        Graphics2D[] temp_graphic_arr = new Graphics2D[Size];
        for(int i = 0; i < Size; ++i)
        {
            temp_graphic_arr[i] = (Graphics2D) graphic_obj.create();
            temp_graphic_arr[i].setColor(obj_color);
            temp_graphic_arr[i].fillRect(dim_arr[i].width, dim_arr[i].height, Obj_height_width, Obj_height_width);
        }
    }
    */
}
