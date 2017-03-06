package com.bracketcove.postrainer;

/**
 * Created by Ryan on 06/03/2017.
 */

/*
public class AlarmFragment extends Fragment implements AlarmController.View {

    private AlarmController.Presenter presenter;


    public AlarmFragment() {

    }

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This is important for Orientation Change handling!!!
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reminder_list, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter == null) {
            presenter = new AlarmPresenter(this,

                    );
        }
        presenter.subscribe();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        presenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void setPresenter(AlarmController.Presenter presenter) {

    }

    @Override
    public void makeToast(String message) {

    }
}*/
