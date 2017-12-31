package ru.spbau.mit.circuit.controler;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.storage.LoadingException;

/**
 * This class stores some tasks to work with converter. Every task is AsyncTask which will be called
 * in another thread, therefore MainActivity thread will not be paused.
 * <p>
 * Class has number of inner tasks and and static fabric methods to create them.
 * <p>
 * Every task requires converter and mode to work with.
 */
class Tasks {

    //FIXME handle exceptions!

    /**
     * The method takes model as an argument.
     * <p>
     * Returned task saves circuit, represented by model, to the storage.
     * <p>
     * Requires target file name to execute.
     */
    static SaveTask saveTask(Converter.Mode mode, Converter converter, Model model) {
        return new SaveTask(mode, converter, model);
    }

    /**
     * Returned task returns all saved circuits from the storage.
     */
    static GetCircuitsTask getCircuitsTask(Converter.Mode mode, Converter converter) {
        return new GetCircuitsTask(mode, converter);
    }

    /**
     * Returned task loads circuit from the storage.
     * <p>
     * Requires target file name to execute.
     */
    static LoadTask loadTask(Converter.Mode mode, Converter converter) {
        return new LoadTask(mode, converter);
    }

    /**
     * Returned task deletes circuit from the storage.
     * <p>
     * Requires target filename to execute.
     */
    static DeleteTask deleteTask(Converter.Mode mode, Converter converter) {
        return new DeleteTask(mode, converter);
    }

    static class SaveTask extends AsyncTask<String, Void, Boolean> {
        private final Converter.Mode mode;
        private final Converter converter;
        private final Model model;

        SaveTask(Converter.Mode mode, Converter converter, Model model) {
            this.converter = converter;
            this.mode = mode;
            this.model = model;
        }

        @Override
        public Boolean doInBackground(String... filename) {
            try {
                return converter.save(mode, model, filename[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    static class GetCircuitsTask extends AsyncTask<Void, Void, List<String>> {

        private final Converter converter;
        private final Converter.Mode mode;

        private GetCircuitsTask(Converter.Mode mode, Converter converter) {
            this.mode = mode;
            this.converter = converter;
        }

        @Override
        public List<String> doInBackground(Void... voids) {
            return converter.getCircuits(mode);
        }
    }

    static class LoadTask extends AsyncTask<String, Void, Model> {

        private final Converter.Mode mode;
        private final Converter converter;

        private Throwable t;

        private LoadTask(Converter.Mode mode, Converter converter) {
            this.mode = mode;
            this.converter = converter;
        }

        @Nullable
        @Override
        public Model doInBackground(@NonNull String... filename) {
            if (filename.length != 1) {
                throw new IllegalArgumentException();
            }
            try {
                return converter.load(mode, filename[0]);
            } catch (LoadingException e) {
                t = e;
            }
            return null;
        }
    }

    static class DeleteTask extends AsyncTask<String, Void, Boolean> {

        private final Converter.Mode mode;
        private final Converter converter;

        private Throwable t;

        private DeleteTask(Converter.Mode mode, Converter converter) {
            this.mode = mode;
            this.converter = converter;
        }

        @Override
        public Boolean doInBackground(String... filename) {
            try {
                return converter.delete(mode, filename[0]);
            } catch (LoadingException e) {
                t = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

}
