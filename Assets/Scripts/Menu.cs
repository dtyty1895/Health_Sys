using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Menu : MonoBehaviour
{
    public void Login()
    {
        SceneManager.LoadScene("Home");
    }
    public void Register()
    {
        SceneManager.LoadScene("Home");
    }
    public void home()
    {
        SceneManager.LoadScene("Home");
    }
    public void hometop()
    {
        SceneManager.LoadScene("Home");
    }
    public void setting()
    {
        SceneManager.LoadScene("Setting");
    }
    public void menu()
    {
        SceneManager.LoadScene("Menu-food");
    }
    public void camera()
    {
        SceneManager.LoadScene("Home");
    }
}
