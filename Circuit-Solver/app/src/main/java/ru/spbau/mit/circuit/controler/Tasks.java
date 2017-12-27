package ru.spbau.mit.circuit.controler;


import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import ru.spbau.mit.circuit.model.Model;
import ru.spbau.mit.circuit.storage.Converter;
import ru.spbau.mit.circuit.storage.LoadingException;

class Tasks {

    static LoadTask loadTask(Converter.Mode mode, Converter converter) {
        return new LoadTask(mode, converter);
    }

    static GetCircuitsTask getCircuitsTask(Converter.Mode mode, Converter converter) {
        return new GetCircuitsTask(mode, converter);
    }

    static SaveTask saveTask(Converter.Mode mode, Converter converter, Model model) {
        return new SaveTask(mode, converter, model);
    }

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

        @Override
        public Model doInBackground(String... filename) {
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
