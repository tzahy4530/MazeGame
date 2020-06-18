package View;

import ViewModel.MyViewModel;

import java.util.Observer;

public interface IView extends Observer {
    public void setViewModel (MyViewModel viewModel);
    public void onShowScreen();
}
