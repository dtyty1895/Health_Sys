using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
public class Menu : MonoBehaviour
{
    public GameObject home;
    public GameObject camera;
    public GameObject menu;
    public GameObject setting;


    public void Start()
    {

    }
    public void Update()
    {
        
    }
  
    #region Menu_Lables
    public void Home()
    {
        home.SetActive(true);
        camera.SetActive(false);
        menu.SetActive(false);
        setting.SetActive(false);
    }
    public void _Camera()
    {
        home.SetActive(false);
        camera.SetActive(true);
        menu.SetActive(false);
        setting.SetActive(false);
    }
    public void _Menu()
    {
        home.SetActive(false);
        camera.SetActive(false);
        menu.SetActive(true);
        setting.SetActive(false);
    }
    public void Setting()
    {
        home.SetActive(false);
        camera.SetActive(false);
        menu.SetActive(false);
        setting.SetActive(true);
    }
    #endregion
  
}
