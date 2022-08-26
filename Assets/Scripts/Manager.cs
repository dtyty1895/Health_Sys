using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Manager:MonoBehaviour
{
    public void Login()
    {
        SceneManager.LoadScene("Home");
    }
    public void Register()
    {
        SceneManager.LoadScene("Register");
    }
}
